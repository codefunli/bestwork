package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.AirWayBillAttachReqDto;
import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.AirWayBillRepository;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IPostInvoiceService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.services.SftpFileService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class AirWayBillServiceImpl implements IAirWayBillService {

	@Autowired
	private AirWayBillRepository airWayBillRepository;

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	SftpFileService sftpFileService;

	@Autowired
	IStorageService iStorageService;

	@Autowired
	IPostInvoiceService iPostInvoiceService;

	@Override
	public void saveAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException {
		AirWayBill airway = new AirWayBill();
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		this.validateAirWayBill(airWayBillReqDto);
		try {
			airway.setCode(airWayBillReqDto.getCode());
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

	@Override
	@Transactional
	public void update(List<MultipartFile> mFiles, PostInvoiceReqDto postInvoiceReqDto, String airWayCode)
			throws BestWorkBussinessException {
		PostInvoice createPostInvoice = null;
		try {
			// Save information for post invoice
			createPostInvoice = iPostInvoiceService.savePostInvoice(postInvoiceReqDto);
			long postInvoiceId = createPostInvoice.getId();
			// Upload file of post invoice into sever
			for (MultipartFile mFile : mFiles) {
				String pathServer = sftpFileService.uploadInvoice(mFile, airWayCode);
				// Save path file of post invoice
				iStorageService.storeFilePostInvoice(postInvoiceId, pathServer);
			}
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0002, null);
		}

	}

	public void validateAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException {
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

}
