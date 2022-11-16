package com.nineplus.bestwork.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.ConstructionListIdDto;
import com.nineplus.bestwork.dto.ConstructionReqDto;
import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.RPageDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.entity.ConstructionEntity;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.ConstructionRepository;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IConstructionService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;
import com.nineplus.bestwork.utils.Enums.ConstructionStatus;
import com.nineplus.bestwork.utils.UserAuthUtils;

/**
 * 
 * @author DiepTT
 *
 */
@Service
public class ConstructionServiceImpl implements IConstructionService {

	@Autowired
	private ConstructionRepository constructionRepository;

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	private ConvertResponseUtils convertResponseUtils;

	@Autowired
	private IAirWayBillService airWayBillService;

	@Autowired
	private IProjectService projectService;

	/**
	 * Function: get page of constructions with condition
	 */
	@Override
	public PageResDto<ConstructionResDto> getPageConstructions(PageSearchDto pageSearchDto)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();

		try {
			Pageable pageable = convertSearch(pageSearchDto);
			List<ProjectEntity> projectList = getProjectsBeingInvolvedByCurrentUser(curUsername);
			List<String> projectIds = new ArrayList<>();
			for (ProjectEntity project : projectList) {
				projectIds.add(project.getId());
			}
			Page<ConstructionEntity> pageConstructionsBeingInvolvedByCurrentUser = constructionRepository
					.findConstructionsByProjectIds(projectIds, pageSearchDto, pageable);

			PageResDto<ConstructionResDto> pageResDto = new PageResDto<>();
			RPageDto metaData = new RPageDto();
			metaData.setNumber(pageConstructionsBeingInvolvedByCurrentUser.getNumber());
			metaData.setSize(pageConstructionsBeingInvolvedByCurrentUser.getSize());
			metaData.setTotalElements(pageConstructionsBeingInvolvedByCurrentUser.getTotalElements());
			metaData.setTotalPages(pageConstructionsBeingInvolvedByCurrentUser.getTotalPages());
			pageResDto.setMetaData(metaData);

			List<ConstructionResDto> constructionResDtos = new ArrayList<>();
			for (ConstructionEntity construction : pageConstructionsBeingInvolvedByCurrentUser.getContent()) {
				ConstructionResDto dto = new ConstructionResDto();
				dto.setId(construction.getId());
				dto.setName(construction.getName());
				dto.setDescription(construction.getDescription());
				dto.setLocation(construction.getLocation());
				dto.setStartDate(construction.getStartDate());
				dto.setEndDate(construction.getEndDate());
				dto.setStatus(construction.getStatus());
				dto.setCreateBy(construction.getCreateBy());
				List<String> awbCodes = new ArrayList<>();
				for (AirWayBill awb : construction.getAirWayBills()) {
					awbCodes.add(awb.getCode());
				}
				dto.setAwbCodes(awbCodes);
				constructionResDtos.add(dto);
			}
			pageResDto.setContent(constructionResDtos);

			return pageResDto;
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	/**
	 * Private function: get userAuthDetected
	 * 
	 * @return UserAuthDetected
	 * @throws BestWorkBussinessException
	 */
	private UserAuthDetected getUserAuthRoleReq() throws BestWorkBussinessException {
		return userAuthUtils.getUserInfoFromReq(false);
	}

	/**
	 * Private function: convert from PageSearchDto to Pageable and search condition
	 * 
	 * @param pageSearchDto
	 * @return Pageable
	 */
	private Pageable convertSearch(PageSearchDto pageSearchDto) {
		if (pageSearchDto.getKeyword().equals("")) {
			pageSearchDto.setKeyword("%%");
		} else {
			pageSearchDto.setKeyword("%" + pageSearchDto.getKeyword() + "%");
		}
		if (pageSearchDto.getStatus() < 0 || pageSearchDto.getStatus() >= ConstructionStatus.values().length) {
			pageSearchDto.setStatus(-1);
		}
		String mappedColumn = convertResponseUtils.convertResponseConstruction(pageSearchDto.getSortBy());
		return PageRequest.of(Integer.parseInt(pageSearchDto.getPage()), Integer.parseInt(pageSearchDto.getSize()),
				Sort.by(pageSearchDto.getSortDirection(), mappedColumn));
	}

	/**
	 * Private function: get all projects that current user is being involved
	 * (creating or/and being assigned)
	 * 
	 * @param curUsername
	 * @return List<ProjectEntity>
	 */
	private List<ProjectEntity> getProjectsBeingInvolvedByCurrentUser(String curUsername) {
		List<ProjectEntity> creatingProjectList = projectService.getProjectsBeingCreatedByCurrentUser(curUsername);
		List<ProjectEntity> assignedProjectList = projectService.getProjectsBeingAssignedToCurrentUser(curUsername);
		Set<ProjectEntity> projectSet = new HashSet<>();
		if (creatingProjectList != null)
			projectSet.addAll(creatingProjectList);
		if (assignedProjectList != null)
			projectSet.addAll(assignedProjectList);
		return new ArrayList<>(projectSet);
	}

	/**
	 * Function: create construction
	 * 
	 * @param constructionReqDto
	 */
	@Override
	public void createConstruction(ConstructionReqDto constructionReqDto) throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = this.getUserAuthRoleReq();
		String curUsername = userAuthDetected.getUsername();

		if (!userAuthDetected.getIsContractor()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		checkExistConstructionNameWhenCreating(constructionReqDto);
		validateConstructionInfo(constructionReqDto);

		ConstructionEntity construction = new ConstructionEntity();
		construction.setCreateBy(curUsername);
		transferAndSaveConstruction(constructionReqDto, construction);
	}

	/**
	 * Private function: transfer from ConstructionReqDto to ConstructionEntity and
	 * save ConstructionEntity to database
	 * 
	 * @param constructionReqDto
	 * @param construction
	 */
	private void transferAndSaveConstruction(ConstructionReqDto constructionReqDto, ConstructionEntity construction) {
		construction.setName(constructionReqDto.getName());
		construction.setDescription(constructionReqDto.getDescription());
		construction.setLocation(constructionReqDto.getLocation());
		construction.setStartDate(constructionReqDto.getStartDate());
		construction.setEndDate(constructionReqDto.getEndDate());
		construction.setStatus(constructionReqDto.getStatus());
		List<AirWayBill> airWayBills = new ArrayList<>();
		for (String code : constructionReqDto.getAwbCodes()) {
			AirWayBill awb = this.airWayBillService.findByCode(code);
			airWayBills.add(awb);
		}
		construction.setAirWayBills(airWayBills);
		this.constructionRepository.save(construction);
	}

	/**
	 * Private function: check the validation of constructionReqDto before creating
	 * new construction
	 * 
	 * @param constructionReqDto
	 * @throws BestWorkBussinessException
	 */
	private void validateConstructionInfo(ConstructionReqDto constructionReqDto) throws BestWorkBussinessException {
		String constructionName = constructionReqDto.getName();
		String[] awbCodes = constructionReqDto.getAwbCodes();

		// Check construction name: not blank
		if (ObjectUtils.isEmpty(constructionName)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.CONSTRUCTION_NAME });
		}

		// Check AWB codes: not blank
		if (ObjectUtils.isEmpty(awbCodes)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.AIR_WAY_BILL });
		}

		// Check existence of AWB codes
		Set<ProjectEntity> projectSetContainingAWBs = new HashSet<>();
		for (String code : awbCodes) {
			AirWayBill airWayBill = this.airWayBillService.findByCode(code);
			if (ObjectUtils.isEmpty(airWayBill)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0004,
						new Object[] { "AWB code " + code });
			}
			Optional<ProjectEntity> projectOpt = this.projectService.getProjectById(airWayBill.getProjectCode());
			if (projectOpt.isPresent()) {
				projectSetContainingAWBs.add(projectOpt.get());
			}
		}

		// Check if all the allocated AWB codes exist in the same project or not
		if (projectSetContainingAWBs.size() > 1) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0005, null);
		} else if (projectSetContainingAWBs.size() == 0) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0006, null);
		}

		// Check if user who is not assigned to a project can not use AWB from that
		// project
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		List<ProjectEntity> involvedProjectList = this.getProjectsBeingInvolvedByCurrentUser(curUsername);
		if (!involvedProjectList.containsAll(projectSetContainingAWBs)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0007, null);
		}

		// Check if the AWB list for the construction contains at least 1 bill that is
		// already customs cleared
		if (!checkAWBStatus(constructionReqDto.getAwbCodes())) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECS0003, null);
		}
	}

	/**
	 * Private function: check if the air way bill list for the construction
	 * contains at least one bill that is already customs cleared or not
	 * 
	 * @param awbCodes
	 * @return true/false
	 */
	private Boolean checkAWBStatus(String[] awbCodes) {
		for (String code : awbCodes) {
			AirWayBill airWayBill = this.airWayBillService.findByCode(code);
			if (AirWayBillStatus.values()[airWayBill.getStatus()].equals(AirWayBillStatus.DONE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Private function: Check new construction name cannot be the same as existed
	 * construction's when creating new construction
	 * 
	 * @param constructionReqDto
	 * @throws BestWorkBussinessException
	 */
	private void checkExistConstructionNameWhenCreating(ConstructionReqDto constructionReqDto)
			throws BestWorkBussinessException {
		ConstructionEntity existedConstruction = constructionRepository.findByName(constructionReqDto.getName());
		if (!ObjectUtils.isEmpty(existedConstruction)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0003,
					new Object[] { CommonConstants.Character.CONSTRUCTION_NAME });
		}
	}

	/**
	 * Private function: Check new construction name cannot be the same as existed
	 * construction's when updating construction
	 * 
	 * @param constructionReqDto
	 * @param curConstruction
	 * @throws BestWorkBussinessException
	 */
	private void checkExistConstructionNameWhenEditing(ConstructionReqDto constructionReqDto,
			ConstructionEntity curConstruction) throws BestWorkBussinessException {
		ConstructionEntity existedConstruction = constructionRepository.findByName(constructionReqDto.getName());
		if (!ObjectUtils.isEmpty(existedConstruction)
				&& !curConstruction.getName().equals(existedConstruction.getName())) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0003,
					new Object[] { CommonConstants.Character.CONSTRUCTION_NAME });
		}
	}

	/**
	 * Function: get detailed construction by construction id and user involved in
	 * the project
	 * 
	 * @param constructionId
	 * @return ConstructionResDto
	 * @throws BestWorkBussinessException
	 */
	@Override
	public ConstructionResDto findConstructionById(long constructionId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();

		Optional<ConstructionEntity> constructionOpt = constructionRepository.findById(constructionId);
		if (!constructionOpt.isPresent()) {
			return null;
		}
		if (!checkIfCurrentUserCanViewConstruction(constructionId, curUsername)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		ConstructionResDto constructionResDto = new ConstructionResDto();
		constructionResDto.setId(constructionId);
		constructionResDto.setName(constructionOpt.get().getName());
		constructionResDto.setDescription(constructionOpt.get().getDescription());
		constructionResDto.setLocation(constructionOpt.get().getLocation());
		constructionResDto.setStartDate(constructionOpt.get().getStartDate());
		constructionResDto.setCreateBy(constructionOpt.get().getCreateBy());
		constructionResDto.setStatus(constructionOpt.get().getStatus());
		List<String> awbCodes = new ArrayList<>();
		for (AirWayBill airWayBill : constructionOpt.get().getAirWayBills()) {
			awbCodes.add(airWayBill.getCode());
		}
		constructionResDto.setAwbCodes(awbCodes);

		return constructionResDto;
	}

	/**
	 * Private function: get project that contains the current construction
	 * 
	 * @param constructionId
	 * @return ProjectEntity
	 */
	private ProjectEntity getProjectContainingCurrentConstruction(long constructionId) {
		ProjectEntity project = this.projectService.getProjectByConstructionId(constructionId);
		return project;
	}

	/**
	 * Private function: check if a specific user can view a specific construction
	 * or not
	 * 
	 * @param constructionId
	 * @param username
	 * @return true/false
	 */
	private Boolean checkIfCurrentUserCanViewConstruction(long constructionId, String username) {
		ProjectEntity currentProject = this.getProjectContainingCurrentConstruction(constructionId);
		List<ProjectEntity> projectListInvolvedByUser = this.getProjectsBeingInvolvedByCurrentUser(username);
		if (projectListInvolvedByUser.contains(currentProject)) {
			return true;
		}
		return false;
	}

	/**
	 * Private function: check if a specific user can edit a specific construction
	 * or not
	 * 
	 * @param construction
	 * @param username
	 * @return true/false
	 */
	private Boolean checkIfCurrentUserCanEditAndDeleteConstruction(ConstructionEntity construction, String username) {
		if (construction.getCreateBy().equals(username)) {
			return true;
		}
		return false;
	}

	/**
	 * Function: update construction by constructionId
	 * 
	 * @param constructionId
	 * @param constructionReqDto
	 */
	@Override
	public void updateConstruction(long constructionId, ConstructionReqDto constructionReqDto)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		Optional<ConstructionEntity> constructionOpt = constructionRepository.findById(constructionId);
		if (!constructionOpt.isPresent()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
		ConstructionEntity curConstruction = constructionOpt.get();
		if (!checkIfCurrentUserCanEditAndDeleteConstruction(curConstruction, curUsername)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		checkExistConstructionNameWhenEditing(constructionReqDto, curConstruction);
		validateConstructionInfo(constructionReqDto);
		transferAndSaveConstruction(constructionReqDto, curConstruction);
	}

	/**
	 * Function: delete 1 construction by construction id
	 * 
	 * @param constructionId
	 */
	@Override
	public void deleteConstruction(ConstructionListIdDto constructionIds) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		for (long id : constructionIds.getListId()) {
			Optional<ConstructionEntity> constructionOpt = constructionRepository.findById(id);
			if (!constructionOpt.isPresent()) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
			}
			ConstructionEntity curConstruction = constructionOpt.get();
			if (!checkIfCurrentUserCanEditAndDeleteConstruction(curConstruction, curUsername)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
			}
			this.constructionRepository.deleteById(id);
		}
	}
}
