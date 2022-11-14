package com.nineplus.bestwork.services.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.dto.PostInvoiceResDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.PostInvoiceRepository;
import com.nineplus.bestwork.services.IInvoicePostService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.services.SftpFileService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class InvoicePostServiceImpl implements IInvoicePostService {

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	PostInvoiceRepository postInvoiceRepository;

	@Autowired
	SftpFileService sftpFileService;

	@Autowired
	IStorageService iStorageService;

	@Override
	@Transactional
	public PostInvoice savePostInvoice(PostInvoiceReqDto postInvoiceReqDto, String airWayBillCode)
			throws BestWorkBussinessException {
		PostInvoice postInvoce = new PostInvoice();
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		try {
			postInvoce.setAirWayBill(airWayBillCode);
			postInvoce.setDescription(postInvoiceReqDto.getDescription());
			postInvoce.setComment(postInvoiceReqDto.getComment());
			postInvoce.setCreateBy(userAuthRoleReq.getUsername());
			postInvoce.setUpdateBy(userAuthRoleReq.getUsername());
			postInvoce.setCreateDate(LocalDateTime.now());
			postInvoce.setUpdateDate(LocalDateTime.now());
			postInvoiceRepository.save(postInvoce);
			return postInvoiceRepository.save(postInvoce);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0001, null);
		}

	}

	@Override
	@Transactional
	public void updatePostInvoice(List<MultipartFile> mFiles, PostInvoiceReqDto postInvoiceReqDto, String airWayCode)
			throws BestWorkBussinessException {
		PostInvoice createPostInvoice = null;
		try {
			// Save information for post invoice
			createPostInvoice = this.savePostInvoice(postInvoiceReqDto, airWayCode);
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

	@Override
	public PostInvoice getPostInvoice(String airWayBillcode) throws BestWorkBussinessException {
		return postInvoiceRepository.findByAirWayBill(airWayBillcode);
	}

	@Override
	public PostInvoiceResDto getDetailInvoice(String airWayBillCode) throws BestWorkBussinessException {
		PostInvoiceResDto postInvoiceResDto = new PostInvoiceResDto();
		PostInvoice invoice = this.getPostInvoice(airWayBillCode);
		if (ObjectUtils.isNotEmpty(invoice)) {
			postInvoiceResDto.setId(invoice.getId());
			postInvoiceResDto.setPostInvoiceCode(invoice.getPostInvoiceCode());
			postInvoiceResDto.setComment(invoice.getComment());
			postInvoiceResDto.setDescription(invoice.getDescription());
			postInvoiceResDto.setCreateBy(invoice.getCreateBy());
			postInvoiceResDto.setUpdateBy(invoice.getUpdateBy());
			postInvoiceResDto.setCreateDate(invoice.getCreateDate());
			postInvoiceResDto.setUpdateDate(invoice.getCreateDate());
			List<FileStorageResDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : invoice.getFileStorages()) {
				FileStorageResDto fileStorageResponseDto = new FileStorageResDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate().toString());
				fileStorageResponseDto.setType(file.getType());
				String pathServer = file.getPathFileServer();
				byte[] fileContent = sftpFileService.downloadFile(pathServer);
				String fileEncoded = Base64.getEncoder().encodeToString(fileContent);
				fileStorageResponseDto.setContent(fileEncoded);
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			postInvoiceResDto.setFileStorages(fileStorageResponseDtos);
		}
		return postInvoiceResDto;
	}
}
