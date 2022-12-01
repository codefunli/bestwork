package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.CustomClearanceInvoiceFileResDto;
import com.nineplus.bestwork.dto.CustomClearancePackageFileResDto;
import com.nineplus.bestwork.dto.CustomClearanceResDto;
import com.nineplus.bestwork.dto.NotificationReqDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.entity.AssignTaskEntity;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.AirWayBillRepository;
import com.nineplus.bestwork.repository.AssignTaskRepository;
import com.nineplus.bestwork.repository.InvoiceFileProjection;
import com.nineplus.bestwork.repository.PackageFileProjection;
import com.nineplus.bestwork.repository.PackagePostRepository;
import com.nineplus.bestwork.repository.PostInvoiceRepository;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IInvoicePostService;
import com.nineplus.bestwork.services.IPackagePostService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.ISftpFileService;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;
import com.nineplus.bestwork.utils.Enums.TRole;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class AirWayBillServiceImpl implements IAirWayBillService {

	@Autowired
	private AirWayBillRepository airWayBillRepository;

	@Autowired
	private IProjectService iProjectService;

	@Autowired
	private IInvoicePostService iInvoicePostService;

	@Autowired
	IPackagePostService iPackagePostService;

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	ISftpFileService iSftpFileService;

	@Autowired
	PostInvoiceRepository postInvoiceRepository;

	@Autowired
	PackagePostRepository packagePostRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AssignTaskRepository assignTaskRepository;

	@Autowired
	MessageUtils messageUtils;

	@Autowired
	NotificationService notifyService;

	@Autowired
	UserService userService;

	@Override
	public AirWayBill findByCode(String code) {
		return this.airWayBillRepository.findByCode(code);
	}

	@Override
	public void saveAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException {
		AirWayBill airway = new AirWayBill();
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		this.validateAirWayBill(airWayBillReqDto);
		try {
			airway.setCode(airWayBillReqDto.getCode());
			airway.setProjectCode(airWayBillReqDto.getProjectId());
			airway.setNote(airWayBillReqDto.getNote());
			airway.setStatus(airWayBillReqDto.getStatus());
			airway.setCreateBy(userAuthRoleReq.getUsername());
			airway.setUpdateBy(userAuthRoleReq.getUsername());
			airway.setCreateDate(LocalDateTime.now());
			airWayBillRepository.save(airway);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0001, null);
		}
		this.sendNotify(airway, true, false);

	}

	private void validateAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException {
		String projectId = airWayBillReqDto.getProjectId();
		if (StringUtils.isEmpty(projectId)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0006, null);
		}
		Optional<ProjectEntity> project = iProjectService.getProjectById(projectId);
		if (!project.isPresent()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0005, null);

		}
		String airWayCode = airWayBillReqDto.getCode();
		// Air Way Bill code can not be empty
		if (ObjectUtils.isEmpty(airWayCode)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0003, null);
		}
		// Check if air way bill code already exist
		AirWayBill airWayBill = airWayBillRepository.findByCode(airWayCode);
		if (!ObjectUtils.isEmpty(airWayBill)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0004, null);
		}

	}

	/**
	 * Function: create notify when an AWB is created or is customs cleared
	 * 
	 * @param airWayBill
	 * @param isCrtAwb     (boolean: true when AWB is created)
	 * @param isAwbCleared (boolean: true when AWB is customs cleared)
	 * @throws BestWorkBussinessException
	 */
	private void sendNotify(AirWayBill airWayBill, boolean isCrtAwb, boolean isAwbCleared)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = userAuthUtils.getUserInfoFromReq(false);
		String curUsername = userAuthDetected.getUsername();
		UserEntity curUser = this.userService.findUserByUsername(curUsername);
		ProjectEntity curPrj = this.iProjectService.getProjectById(airWayBill.getProjectCode()).get();

		List<AssignTaskEntity> assignTaskList = this.assignTaskRepository.findByProjectId(curPrj.getId());

		String title = "";
		String content = "";
		// Set the title and content for the notify when AWB is created 
		if (isCrtAwb) {
			title = messageUtils.getMessage(CommonConstants.MessageCode.TNU0009,
					new Object[] { curPrj.getProjectName() });
			content = messageUtils.getMessage(CommonConstants.MessageCode.CNU0009,
					new Object[] { curUsername, airWayBill.getCode() });
		}
		// Set the title and content for the notify when AWB is customs cleared 
		else if (isAwbCleared) {
			title = messageUtils.getMessage(CommonConstants.MessageCode.TNU0010,
					new Object[] { curPrj.getProjectName() });
			content = messageUtils.getMessage(CommonConstants.MessageCode.CNU0010,
					new Object[] { airWayBill.getCode() });
		}

		NotificationReqDto notifyReqDto = new NotificationReqDto();
		notifyReqDto.setTitle(title);
		notifyReqDto.setContent(content);
		Set<Long> userIdList = new HashSet<>();

		// Get userId of project-creator (investor)
		UserEntity userCrtPrj = this.userService.findUserByUsername(curPrj.getCreateBy());
		userIdList.add(userCrtPrj.getId());
		// Get userId of AWB-creator (supplier)
		if (isAwbCleared) {
			userIdList.add(this.userService.findUserByUsername(airWayBill.getCreateBy()).getId());
		}
		// Get userIds of the contractors who are assigned as editors in curProject
		// (contractors)
		for (AssignTaskEntity ast : assignTaskList) {
			if (ast.isCanEdit() && this.userService.findUserByUserId(ast.getUserId()).getRole().getRoleName()
					.equals(TRole.CONTRACTOR.getValue())) {
				userIdList.add(ast.getUserId());
			}
		}
		userIdList.removeIf(x -> x == curUser.getId());
		for (Long uId : userIdList) {
			notifyReqDto.setUserId(uId);
			notifyService.createNotification(notifyReqDto);
		}
	}

	@Override
	public List<AirWayBillResDto> getAllAirWayBillByProject(String projectId) throws BestWorkBussinessException {
		List<AirWayBill> listAwb = airWayBillRepository.findByProjectCode(projectId);
		List<AirWayBillResDto> listAwbRes = new ArrayList<>();
		for (AirWayBill airWayBill : listAwb) {
			AirWayBillResDto airWayResDTO = new AirWayBillResDto();
			airWayResDTO = modelMapper.map(airWayBill, AirWayBillResDto.class);
			airWayResDTO.setStatus(AirWayBillStatus.convertIntToStatus(airWayBill.getStatus()));
			listAwbRes.add(airWayResDTO);
		}
		return listAwbRes;
	}

	@Override
	public AirWayBillResDto getDetail(String airWayBillCode) throws BestWorkBussinessException {
		AirWayBillResDto airWayResDTO = null;
		AirWayBill airway = airWayBillRepository.findByCode(airWayBillCode);
		if (ObjectUtils.isNotEmpty(airway)) {
			airWayResDTO = modelMapper.map(airway, AirWayBillResDto.class);
		}
		return airWayResDTO;
	}

	@Override
	public CustomClearanceResDto getCustomClearanceDoc(String code) throws BestWorkBussinessException {
		CustomClearanceResDto res = new CustomClearanceResDto();
		List<CustomClearanceInvoiceFileResDto> invoiceInfo = iInvoicePostService.getInvoiceClearance(code);
		if (ObjectUtils.isNotEmpty(invoiceInfo)) {
			res.setInvoicesDoc(invoiceInfo);
		}
		List<CustomClearancePackageFileResDto> packageInfo = iPackagePostService.getPackageClearance(code);
		if (ObjectUtils.isNotEmpty(packageInfo)) {
			res.setPackagesDoc(packageInfo);
		}
		return res;
	}

	@Override
	public List<String> createZipFolder(String code) throws BestWorkBussinessException {
		List<String> listPathToDownLoad = new ArrayList<>();
		List<InvoiceFileProjection> invoiceInfo = postInvoiceRepository.getClearanceInfo(code);
		List<PackageFileProjection> packageInfo = packagePostRepository.getClearancePackageInfo(code);

		if (ObjectUtils.isNotEmpty(invoiceInfo)) {
			for (InvoiceFileProjection invoice : invoiceInfo) {
				listPathToDownLoad.add(invoice.getPathFileServer());
			}
		}
		if (ObjectUtils.isNotEmpty(packageInfo)) {
			for (PackageFileProjection pack : packageInfo) {
				listPathToDownLoad.add(pack.getPathFileServer());
			}
		}
		return this.iSftpFileService.downloadFileTemp(code, listPathToDownLoad);
	}

	@Override
	@Transactional
	public void changeStatus(String code, int destinationStatus) throws BestWorkBussinessException {
		this.airWayBillRepository.changeStatus(code, destinationStatus);
		if (destinationStatus == AirWayBillStatus.DONE.ordinal()) {
			this.sendNotify(airWayBillRepository.findByCode(code), false, true);
		}
	}

}
