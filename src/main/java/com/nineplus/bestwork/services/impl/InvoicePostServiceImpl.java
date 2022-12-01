package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.CustomClearanceInvoiceFileResDto;
import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.dto.PostInvoiceResDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.InvoiceFileProjection;
import com.nineplus.bestwork.repository.PostInvoiceRepository;
import com.nineplus.bestwork.services.IInvoicePostService;
import com.nineplus.bestwork.services.ISftpFileService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.FolderType;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class InvoicePostServiceImpl implements IInvoicePostService {

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	PostInvoiceRepository postInvoiceRepository;

	@Autowired
	ISftpFileService sftpFileService;

	@Autowired
	IStorageService iStorageService;

	@Override
	@Transactional
	public PostInvoice savePostInvoice(PostInvoiceReqDto postInvoiceReqDto, String airWayBillCode)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		PostInvoice postInvoce = new PostInvoice();
		try {
			postInvoce.setAirWayBill(airWayBillCode);
			postInvoce.setDescription(postInvoiceReqDto.getDescription());
			postInvoce.setCreateBy(userAuthRoleReq.getUsername());
			postInvoce.setUpdateBy(userAuthRoleReq.getUsername());
			postInvoce.setCreateDate(LocalDateTime.now());
			postInvoce.setUpdateDate(LocalDateTime.now());
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
		// Validate file
		if (!sftpFileService.isValidFile(mFiles)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0002, null);
		}
		try {
			// Save information for post invoice
			createPostInvoice = this.savePostInvoice(postInvoiceReqDto, airWayCode);
			long postInvoiceId = createPostInvoice.getId();
			// Upload file of post invoice into sever
			for (MultipartFile mFile : mFiles) {
				String pathServer = sftpFileService.uploadInvoice(mFile, airWayCode, postInvoiceId);
				// Save path file of post invoice
				iStorageService.storeFile(postInvoiceId, FolderType.INVOICE, pathServer);
			}
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0002, null);
		}

	}

	@Override
	public Optional<PostInvoice> getPostInvoice(Long invoicePostId) throws BestWorkBussinessException {
		return postInvoiceRepository.findById(invoicePostId);
	}

	@Override
	public PostInvoiceResDto getDetailInvoice(Long invoicePostId) throws BestWorkBussinessException {
		PostInvoiceResDto postInvoiceResDto = null;
		Optional<PostInvoice> invoice = this.getPostInvoice(invoicePostId);
		if (invoice.isPresent()) {
			postInvoiceResDto = new PostInvoiceResDto();
			postInvoiceResDto.setId(invoice.get().getId());
			postInvoiceResDto.setComment(invoice.get().getComment());
			postInvoiceResDto.setDescription(invoice.get().getDescription());
			postInvoiceResDto.setCreateBy(invoice.get().getCreateBy());
			postInvoiceResDto.setUpdateBy(invoice.get().getUpdateBy());
			postInvoiceResDto.setCreateDate(invoice.get().getCreateDate().toString());
			postInvoiceResDto.setUpdateDate(invoice.get().getUpdateDate().toString());
			List<FileStorageResDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : invoice.get().getFileStorages()) {
				FileStorageResDto fileStorageResponseDto = new FileStorageResDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate().toString());
				fileStorageResponseDto.setType(file.getType());
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			postInvoiceResDto.setFileStorages(fileStorageResponseDtos);
		}
		return postInvoiceResDto;
	}

	@Override
	public List<PostInvoiceResDto> getAllInvoicePost(String airWayBillId) throws BestWorkBussinessException {
		List<PostInvoice> listInvoicePost = postInvoiceRepository.findByAirWayBill(airWayBillId);
		List<PostInvoiceResDto> listPostInvoiceResDto = new ArrayList<>();
		PostInvoiceResDto res = null;
		for (PostInvoice invoice : listInvoicePost) {
			res = new PostInvoiceResDto();
			res.setId(invoice.getId());
			res.setComment(invoice.getComment());
			res.setDescription(invoice.getDescription());
			res.setCreateBy(invoice.getCreateBy());
			res.setUpdateBy(invoice.getUpdateBy());
			res.setCreateDate(invoice.getCreateDate().toString());
			res.setUpdateDate(invoice.getUpdateDate().toString());
			res.setPostType(CommonConstants.Character.TYPE_POST_INVOICE);
			List<FileStorageResDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : invoice.getFileStorages()) {
				FileStorageResDto fileStorageResponseDto = new FileStorageResDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate().toString());
				fileStorageResponseDto.setType(file.getType());
				fileStorageResponseDto.setChoosen(file.isChoosen());
				// return content file if file is image
				if (Arrays.asList(new String[] { "png", "jpg", "jpeg", "bmp", "JPEG" }).contains(file.getType())) {
					String pathServer = file.getPathFileServer();
					byte[] imageContent = sftpFileService.getFile(pathServer);
					fileStorageResponseDto.setContent(imageContent);
				}
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			res.setFileStorages(fileStorageResponseDtos);

			listPostInvoiceResDto.add(res);
			// Sort by newest create date
			if (ObjectUtils.isNotEmpty(listPostInvoiceResDto)) {
				listPostInvoiceResDto.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
			}
		}
		return listPostInvoiceResDto;
	}

	@Override
	public byte[] getFile(Long postId, Long fileId) throws BestWorkBussinessException {
		String pathFile = this.getPathFileToDownload(postId, fileId);
		byte[] fileContent = sftpFileService.getFile(pathFile);
		return fileContent;
	}

	@Override
	public String getPathFileToDownload(Long postId, Long fileId) {
		return postInvoiceRepository.getPathFileServer(postId, fileId);
	}

	@Override
	public List<CustomClearanceInvoiceFileResDto> getInvoiceClearance(String code) throws BestWorkBussinessException {
		List<CustomClearanceInvoiceFileResDto> lst = new ArrayList<>();
		CustomClearanceInvoiceFileResDto customClearanceFileResDto = null;
		List<InvoiceFileProjection> res = postInvoiceRepository.getClearanceInfo(code);
		for (InvoiceFileProjection projection : res) {
			customClearanceFileResDto = new CustomClearanceInvoiceFileResDto();
			customClearanceFileResDto.setFileId(projection.getFileId());
			customClearanceFileResDto.setPostInvoiceId(projection.getPostInvoiceId());
			customClearanceFileResDto.setName(projection.getName());
			customClearanceFileResDto.setType(projection.getType());
			if (Arrays.asList(new String[] { "png", "jpg", "jpeg", "bmp", "JPEG" }).contains(projection.getType())) {
				String pathServer = projection.getPathFileServer();
				byte[] imageContent = sftpFileService.getFile(pathServer);
				customClearanceFileResDto.setContent(imageContent);
			}
			customClearanceFileResDto.setPostType(CommonConstants.Character.TYPE_POST_INVOICE);
			lst.add(customClearanceFileResDto);
		}
		return lst;
	}

	@Override
	@Transactional
	public PostInvoice pushComment(Long postInvoiceId, PostCommentReqDto postCommentRequestDto)
			throws BestWorkBussinessException {
		PostInvoice currentPost = null;
		try {
			if (ObjectUtils.isNotEmpty(postInvoiceId) && ObjectUtils.isNotEmpty(postCommentRequestDto)) {
				// Check exist post invoice with air way bill in DB
				currentPost = this.postInvoiceRepository.findByIdAndAirWayBill(postInvoiceId,
						postCommentRequestDto.getAirWayBillCode());
				if (ObjectUtils.isEmpty(currentPost)) {
					throw new BestWorkBussinessException(CommonConstants.MessageCode.eI0003, null);
				}
				// Set comment
				currentPost.setComment(postCommentRequestDto.getComment());
				this.postInvoiceRepository.save(currentPost);
			}
		} catch (Exception e) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eI0004, null);
		}
		return currentPost;
	}

}
