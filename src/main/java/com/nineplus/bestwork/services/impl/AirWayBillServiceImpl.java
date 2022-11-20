package com.nineplus.bestwork.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.CustomClearanceInvoiceFileResDto;
import com.nineplus.bestwork.dto.CustomClearancePackageFileResDto;
import com.nineplus.bestwork.dto.CustomClearanceResDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.AirWayBillRepository;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IInvoicePostService;
import com.nineplus.bestwork.services.IPackagePostService;
import com.nineplus.bestwork.services.IProjectService;
import com.nineplus.bestwork.services.ISftpFileService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;
import com.nineplus.bestwork.utils.UserAuthUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
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
	ModelMapper modelMapper;

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

	@Override
	public List<AirWayBillResDto> getAllAirWayBillByProject(String projectId) throws BestWorkBussinessException {
		List<AirWayBill> listAwb = airWayBillRepository.findByProjectCode(projectId);
		List<AirWayBillResDto> listAwbRes =  new ArrayList<>();
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
		if(ObjectUtils.isNotEmpty(invoiceInfo)) {
			res.setInvoicesDoc(invoiceInfo);
		}
		List<CustomClearancePackageFileResDto> packageInfo = iPackagePostService.getPackageClearance(code);
		if(ObjectUtils.isNotEmpty(packageInfo)) {
			res.setPackagesDoc(packageInfo);
		}
		return res;
	}

	@Override
	public StreamingResponseBody downloadZip(String code, HttpServletResponse response) throws BestWorkBussinessException {
		List<String> listPathToDownLoad = new ArrayList<>();
		listPathToDownLoad.add("/home/bestwork/invoices/20221117/AIRWAY00000001/123/download.png");
		int BUFFER_SIZE = 1024;
		StreamingResponseBody streamResponseBody = out -> {

			final ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
			ZipEntry zipEntry = null;
			InputStream inputStream = null;
			File tempFile;

			try {
				for (String path : listPathToDownLoad) {
					zipEntry = new ZipEntry(code);
					tempFile = File.createTempFile("fileTemp",null);
					tempFile.deleteOnExit();
					tempFile = iSftpFileService.downLoadFile(path);
					inputStream = new FileInputStream(tempFile);
					zipOutputStream.putNextEntry(zipEntry);
					byte[] bytes = new byte[BUFFER_SIZE];
					int length;
					while ((length = inputStream.read(bytes)) >= 0) {
						zipOutputStream.write(bytes, 0, length);
					}

				}

			} catch (IOException e) {
				log.error("Exception while reading and streaming data {} ", e);
			} finally {
				if (zipOutputStream != null) {
					zipOutputStream.close();
				}
			}

		};
		return streamResponseBody;
	}

	@Override
	@Transactional
	public void confirmDone(String code,int destinationStatus) throws BestWorkBussinessException {
		this.airWayBillRepository.changeStatusToDone(code, destinationStatus);
	}
	
}
