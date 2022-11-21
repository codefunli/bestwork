package com.nineplus.bestwork.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.ConstructionListIdDto;
import com.nineplus.bestwork.dto.ConstructionReqDto;
import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.RPageDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.entity.AssignTaskEntity;
import com.nineplus.bestwork.entity.ConstructionEntity;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.AssignTaskRepository;
import com.nineplus.bestwork.repository.ConstructionRepository;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IConstructionService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.ISftpFileService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;
import com.nineplus.bestwork.utils.Enums.ConstructionStatus;
import com.nineplus.bestwork.utils.Enums.FolderType;
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

	@Autowired
	private ISftpFileService sftpFileService;

	@Autowired
	private IStorageService storageService;

	@Autowired
	private AssignTaskRepository assignTaskRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	ModelMapper modelMapper;

	/**
	 * Function: get page of constructions with condition
	 */
	@Override
	public PageResDto<ConstructionResDto> getPageConstructions(PageSearchDto pageSearchDto)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();

		try {
			Pageable pageable = convertSearch(pageSearchDto);

			List<ProjectEntity> canViewprjList = getPrjLstByAnyUsername(userAuthRoleReq);
			List<String> prjIds = new ArrayList<>();
			for (ProjectEntity project : canViewprjList) {
				prjIds.add(project.getId());
			}
			Page<ConstructionEntity> pageCstrt = constructionRepository.findCstrtByPrjIds(prjIds, pageSearchDto,
					pageable);

			PageResDto<ConstructionResDto> pageResDto = new PageResDto<>();
			RPageDto metaData = new RPageDto();
			metaData.setNumber(pageCstrt.getNumber());
			metaData.setSize(pageCstrt.getSize());
			metaData.setTotalElements(pageCstrt.getTotalElements());
			metaData.setTotalPages(pageCstrt.getTotalPages());
			pageResDto.setMetaData(metaData);

			List<ConstructionResDto> constructionResDtos = new ArrayList<>();
			for (ConstructionEntity construction : pageCstrt.getContent()) {
				ConstructionResDto dto = this.transferConstructionToResDto(construction);
				constructionResDtos.add(dto);
			}
			pageResDto.setContent(constructionResDtos);

			return pageResDto;
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	private List<ProjectEntity> getPrjLstByAnyUsername(UserAuthDetected userAuthRoleReq) {
		List<ProjectEntity> canViewprjList = null;
		String curUsername = userAuthRoleReq.getUsername();
		if (!userAuthRoleReq.getIsSysAdmin() && !userAuthRoleReq.getIsOrgAdmin()) {
			canViewprjList = this.getPrjInvolvedByCompUser(curUsername);
		} else if (userAuthRoleReq.getIsOrgAdmin()) {
			canViewprjList = this.projectService.getPrj4CompanyAdmin(curUsername);
		} else if (userAuthRoleReq.getIsSysAdmin()) {
			canViewprjList = this.projectService.getPrj4SysAdmin(curUsername);
		}
		return canViewprjList;
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
	private List<ProjectEntity> getPrjInvolvedByCompUser(String curUsername) {
		List<ProjectEntity> creatingPrjList = projectService.getPrjCreatedByCurUser(curUsername);
		List<ProjectEntity> assignedPrjList = projectService.getPrAssignedToCurUser(curUsername);
		Set<ProjectEntity> projectSet = new HashSet<>();
		if (creatingPrjList != null)
			projectSet.addAll(creatingPrjList);
		if (assignedPrjList != null)
			projectSet.addAll(assignedPrjList);
		return new ArrayList<>(projectSet);
	}

	/**
	 * Function: create construction
	 * 
	 * @param constructionReqDto
	 */
	@Override
	public void createConstruction(ConstructionReqDto constructionReqDto, List<MultipartFile> drawings)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = this.getUserAuthRoleReq();
		String curUsername = userAuthDetected.getUsername();

		if (!chkCurUserCanCreateCstrt(userAuthDetected, constructionReqDto)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		chkExistCstrtNameWhenCreating(constructionReqDto);
		validateConstructionInfo(constructionReqDto);

		ConstructionEntity construction = new ConstructionEntity();
		construction.setCreateBy(curUsername);
		transferDtoToConstruction(constructionReqDto, construction);
		try {
			construction = this.constructionRepository.save(construction);

			if (!sftpFileService.isValidFile(drawings)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0002, null);
			}
			for (MultipartFile file : drawings) {
				String pathServer = this.sftpFileService.uploadConstructionDrawing(file, construction.getId());
				storageService.storeFile(construction.getId(), FolderType.CONSTRUCTION, pathServer);
			}
		} catch (BestWorkBussinessException ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.FILE0002, null);
		}
	}

	/**
	 * Private function: transfer from ConstructionReqDto to ConstructionEntity and
	 * save ConstructionEntity to database
	 * 
	 * @param constructionReqDto
	 * @param construction
	 */
	private void transferDtoToConstruction(ConstructionReqDto constructionReqDto, ConstructionEntity construction) {
		construction.setConstructionName(constructionReqDto.getConstructionName());
		construction.setDescription(constructionReqDto.getDescription());
		construction.setLocation(constructionReqDto.getLocation());
		construction.setStartDate(constructionReqDto.getStartDate());
		construction.setEndDate(constructionReqDto.getEndDate());
		construction.setStatus(constructionReqDto.getStatus());
		construction.setProjectCode(constructionReqDto.getProjectCode());
		List<AirWayBill> airWayBills = new ArrayList<>();
		for (AirWayBillResDto dto : constructionReqDto.getAwbCodes()) {
			AirWayBill awb = this.airWayBillService.findByCode(dto.getCode());
			airWayBills.add(awb);
		}
		construction.setAirWayBills(airWayBills);
	}

	/**
	 * Private function: check the validation of constructionReqDto before creating
	 * new construction
	 * 
	 * @param constructionReqDto
	 * @throws BestWorkBussinessException
	 */
	private void validateConstructionInfo(ConstructionReqDto constructionReqDto) throws BestWorkBussinessException {
		String constructionName = constructionReqDto.getConstructionName();
		List<AirWayBillResDto> awbCodes = constructionReqDto.getAwbCodes();

		// Check construction name: not blank
		if (ObjectUtils.isEmpty(constructionName)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.CONSTRUCTION_NAME });
		}

		// Check if current project (found by project id) exists or not
		Optional<ProjectEntity> curProjectOpt = this.projectService.getProjectById(constructionReqDto.getProjectCode());
		if (!curProjectOpt.isPresent()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0004,
					new Object[] { CommonConstants.Character.PROJECT });
		}
		ProjectEntity curProject = curProjectOpt.get();

		// Check if current user is involved in current project or not
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		List<ProjectEntity> involvedProjectList = this.getPrjInvolvedByCompUser(curUsername);
		if (!involvedProjectList.contains(curProject)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0009, null);
		}

		// Check AWB codes: not blank
		if (ObjectUtils.isEmpty(awbCodes)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.AIR_WAY_BILL });
		}

		// Check existence of AWB codes
		Set<ProjectEntity> projectSetContainingAWBs = new HashSet<>();
		for (AirWayBillResDto awbResdto : awbCodes) {
			String code = awbResdto.getCode();
			AirWayBill airWayBill = this.airWayBillService.findByCode(code);
			if (ObjectUtils.isEmpty(airWayBill)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0004,
						new Object[] { "AWB code " + code });
			}
			Optional<ProjectEntity> projectOpt = this.projectService.getProjectById(airWayBill.getProjectCode());
			if (!projectOpt.isPresent()) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0007,
						new Object[] { "code " + code });
			}
			projectSetContainingAWBs.add(projectOpt.get());
		}

		// Check if all the allocated AWB codes exist in current project or not
		if (projectSetContainingAWBs.size() > 1) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0005, null);
		} else if (projectSetContainingAWBs.size() == 1 && !projectSetContainingAWBs.contains(curProject)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0008, null);
		} else if (projectSetContainingAWBs.size() == 0 || projectSetContainingAWBs.isEmpty()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EXS0006, null);
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
	private Boolean checkAWBStatus(List<AirWayBillResDto> awbCodes) {
		for (AirWayBillResDto awbResDto : awbCodes) {
			String code = awbResDto.getCode();
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
	private void chkExistCstrtNameWhenCreating(ConstructionReqDto constructionReqDto)
			throws BestWorkBussinessException {
		ConstructionEntity existedCstrt = constructionRepository.findByName(constructionReqDto.getConstructionName());
		if (!ObjectUtils.isEmpty(existedCstrt)) {
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
	private void chkExistCstrtNameWhenEditing(ConstructionReqDto constructionReqDto, ConstructionEntity curConstruction)
			throws BestWorkBussinessException {
		ConstructionEntity existedCstrt = constructionRepository.findByName(constructionReqDto.getConstructionName());
		if (!ObjectUtils.isEmpty(existedCstrt)
				&& !curConstruction.getConstructionName().equals(existedCstrt.getConstructionName())) {
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
		Optional<ConstructionEntity> constructionOpt = constructionRepository.findById(constructionId);
		if (!constructionOpt.isPresent()) {
			return null;
		}
		if (!chkCurUserCanViewCstrt(constructionId, userAuthRoleReq)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		ConstructionResDto constructionResDto = transferConstructionToResDto(constructionOpt.get());

		return constructionResDto;
	}

	private ConstructionResDto transferConstructionToResDto(ConstructionEntity cstrt) {
		ConstructionResDto constructionResDto = new ConstructionResDto();
		constructionResDto.setId(cstrt.getId());
		constructionResDto.setConstructionName(cstrt.getConstructionName());
		constructionResDto.setDescription(cstrt.getDescription());
		constructionResDto.setLocation(cstrt.getLocation());
		constructionResDto.setStartDate(cstrt.getStartDate());
		constructionResDto.setCreateBy(cstrt.getCreateBy());
		constructionResDto.setStatus(cstrt.getStatus());
		constructionResDto.setProjectCode(cstrt.getProjectCode());
		List<AirWayBillResDto> awbCodes = new ArrayList<>();
		for (AirWayBill airWayBill : cstrt.getAirWayBills()) {
			AirWayBillResDto dto = new AirWayBillResDto();
			dto = modelMapper.map(airWayBill, AirWayBillResDto.class);
			dto.setStatus(AirWayBillStatus.convertIntToStatus(airWayBill.getStatus()));
			awbCodes.add(dto);
		}
		
		constructionResDto.setAwbCodes(awbCodes);

		List<FileStorageResDto> fileStorageResponseDtos = new ArrayList<>();
		for (FileStorageEntity file : cstrt.getFileStorages()) {
			FileStorageResDto fileStorageResponseDto = new FileStorageResDto();
			fileStorageResponseDto.setId(file.getId());
			fileStorageResponseDto.setName(file.getName());
			fileStorageResponseDto.setCreateDate(file.getCreateDate().toString());
			fileStorageResponseDto.setType(file.getType());
			fileStorageResponseDto.setChoosen(file.isChoosen());
			if (Arrays.asList(CommonConstants.Image.IMAGE_EXTENSION).contains(file.getType())) {
				String pathServer = file.getPathFileServer();
				byte[] imageContent = sftpFileService.getFile(pathServer);
				fileStorageResponseDto.setContent(imageContent);
			}
			fileStorageResponseDtos.add(fileStorageResponseDto);
		}
		constructionResDto.setFileStorages(fileStorageResponseDtos);
		return constructionResDto;
	}

	
	/**
	 * Private function: get project that contains the current construction
	 * 
	 * @param constructionId
	 * @return ProjectEntity
	 */
	private ProjectEntity getPrjByCurCstrt(long constructionId) {
		ProjectEntity project = this.projectService.getProjectByConstructionId(constructionId);
		return project;
	}

	private Boolean chkCurUserCanCreateCstrt(UserAuthDetected userAuthDetected, ConstructionReqDto cstrtReqDto)
			throws BestWorkBussinessException {
		UserEntity curUser = this.userService.findUserByUsername(userAuthDetected.getUsername());
		AssignTaskEntity curAssign = this.assignTaskRepository.findByProjectIdAndUserId(cstrtReqDto.getProjectCode(),
				curUser.getId());
		if (userAuthDetected.getIsContractor() && curAssign.isCanEdit()) {
			return true;
		}
		return false;
	}

	/**
	 * Private function: check if a specific user can view a specific construction
	 * or not
	 * 
	 * @param constructionId
	 * @param userAuthRoleReq
	 * @return true/false
	 */
	private Boolean chkCurUserCanViewCstrt(long constructionId, UserAuthDetected userAuthRoleReq) {
		ProjectEntity curPrj = this.getPrjByCurCstrt(constructionId);
		List<ProjectEntity> prjLstCurUserCanView = this.getPrjLstByAnyUsername(userAuthRoleReq);
		if (prjLstCurUserCanView.contains(curPrj)) {
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
	private Boolean chkCurUserCanEditDelCstrt(ConstructionEntity construction, String username) {
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
	public void updateConstruction(long constructionId, ConstructionReqDto constructionReqDto,
			List<MultipartFile> drawings) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		Optional<ConstructionEntity> constructionOpt = constructionRepository.findById(constructionId);
		if (!constructionOpt.isPresent()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
		ConstructionEntity curConstruction = constructionOpt.get();
		if (!chkCurUserCanEditDelCstrt(curConstruction, curUsername)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		chkExistCstrtNameWhenEditing(constructionReqDto, curConstruction);
		validateConstructionInfo(constructionReqDto);
		transferDtoToConstruction(constructionReqDto, curConstruction);

		try {
			curConstruction = this.constructionRepository.save(curConstruction);

			if (!sftpFileService.isValidFile(drawings)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0002, null);
			}
			for (MultipartFile file : drawings) {
				String pathServer = this.sftpFileService.uploadConstructionDrawing(file, curConstruction.getId());
				storageService.storeFile(curConstruction.getId(), FolderType.CONSTRUCTION, pathServer);
			}
		} catch (BestWorkBussinessException ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.FILE0002, null);
		}
	}

	/**
	 * Function: delete constructions by list of construction ids
	 * 
	 * @param ConstructionListIdDto
	 */
	@Override
	public void deleteConstruction(ConstructionListIdDto constructionIds) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = this.getUserAuthRoleReq();
		String curUsername = userAuthRoleReq.getUsername();
		long[] ids = constructionIds.getListId();
		List<ConstructionEntity> constructionList = constructionRepository.findByIds(ids);
		for (ConstructionEntity construction : constructionList) {
			if (!chkCurUserCanEditDelCstrt(construction, curUsername)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
			}
		}
		if (constructionList == null) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECS0005, null);
		}
		if (constructionList.contains(null)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECS0006, null);
		}
		this.constructionRepository.deleteAll(constructionList);
	}

	@Override
	public ConstructionEntity findCstrtById(long constructionId) {
		Optional<ConstructionEntity> cstrtOpt = this.constructionRepository.findById(constructionId);
		if (!cstrtOpt.isPresent()) {
			return null;
		} else {
			return cstrtOpt.get();
		}
	}
}
