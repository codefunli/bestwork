package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.EvidenceAfterReqDto;
import com.nineplus.bestwork.dto.EvidenceAfterResDto;
import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.entity.EvidenceAfterPost;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.EvidenceAfterPostRepository;
import com.nineplus.bestwork.services.IEvidenAfterPostService;
import com.nineplus.bestwork.services.ISftpFileService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.FolderType;
import com.nineplus.bestwork.utils.UserAuthUtils;

/**
 * 
 * @author TuanNA
 *
 */
@Service
public class EvidenAfterPostServiceImpl implements IEvidenAfterPostService {

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	ISftpFileService sftpFileService;

	@Autowired
	IStorageService iStorageService;

	@Autowired
	EvidenceAfterPostRepository evidenceAfterPostRepository;

	@Override
	@Transactional
	public void updateEvidenceAfter(EvidenceAfterReqDto evidenceAfterReqDto, List<MultipartFile> mFiles)
			throws BestWorkBussinessException {
		EvidenceAfterPost evidenceAfter = null;
		// Validate file
		if (!sftpFileService.isValidFile(mFiles)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0002, null);
		}
		if (!sftpFileService.isImageFile(mFiles)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0004, null);
		}
		try {
			if (ObjectUtils.isNotEmpty(evidenceAfterReqDto)) {
				long awbId = evidenceAfterReqDto.getAirWayBillId();
				// Save information for post invoice
				evidenceAfter = this.saveEvidenceAfter(evidenceAfterReqDto);
				long evidenceBeforePostId = evidenceAfter.getId();
				// Upload file of post invoice into sever
				for (MultipartFile mFile : mFiles) {
					String pathServer = sftpFileService.uploadEvidenceAfter(mFile, awbId, evidenceBeforePostId);
					// Save path file of post invoice
					iStorageService.storeFile(evidenceBeforePostId, FolderType.EVIDENCE_AFTER, pathServer);
				}
			}
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eEA0001, null);
		}
	}

	@Transactional
	private EvidenceAfterPost saveEvidenceAfter(EvidenceAfterReqDto evidenceAfterReqDto)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		EvidenceAfterPost evidenceAfter = new EvidenceAfterPost();
		try {
			evidenceAfter.setAirWayBill(evidenceAfterReqDto.getAirWayBillId());
			evidenceAfter.setDescription(evidenceAfterReqDto.getDescription());
			evidenceAfter.setCreateBy(userAuthRoleReq.getUsername());
			evidenceAfter.setUpdateBy(userAuthRoleReq.getUsername());
			evidenceAfter.setCreateDate(LocalDateTime.now());
			evidenceAfter.setUpdateDate(LocalDateTime.now());
			return evidenceAfterPostRepository.save(evidenceAfter);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eEA0001, null);
		}
	}

	@Override
	public List<EvidenceAfterResDto> getAllEvidenceAfter(long awbId) throws BestWorkBussinessException {
		List<EvidenceAfterPost> listEvidence = evidenceAfterPostRepository.findByAirWayBill(awbId);
		List<EvidenceAfterResDto> listEvidenceResDto = new ArrayList<>();
		EvidenceAfterResDto res = null;
		for (EvidenceAfterPost evidence : listEvidence) {
			res = new EvidenceAfterResDto();
			res.setId(evidence.getId());
			res.setComment(evidence.getComment());
			res.setDescription(evidence.getDescription());
			res.setCreateBy(evidence.getCreateBy());
			res.setUpdateBy(evidence.getUpdateBy());
			res.setCreateDate(evidence.getCreateDate().toString());
			res.setUpdateDate(evidence.getUpdateDate().toString());
			List<FileStorageResDto> fileStorageResponseDtos = new ArrayList<>();
			for (FileStorageEntity file : evidence.getFileStorages()) {
				FileStorageResDto fileStorageResponseDto = new FileStorageResDto();
				fileStorageResponseDto.setId(file.getId());
				fileStorageResponseDto.setName(file.getName());
				fileStorageResponseDto.setCreateDate(file.getCreateDate().toString());
				fileStorageResponseDto.setType(file.getType());
				// return content file if file is image
				if (Arrays.asList(CommonConstants.Image.IMAGE_EXTENSION).contains(file.getType())) {
					String pathServer = file.getPathFileServer();
					byte[] imageContent = sftpFileService.getFile(pathServer);
					fileStorageResponseDto.setContent(imageContent);
				}
				fileStorageResponseDtos.add(fileStorageResponseDto);
			}
			res.setFileStorages(fileStorageResponseDtos);

			listEvidenceResDto.add(res);
			// Sort by newest create date
			if (ObjectUtils.isNotEmpty(listEvidenceResDto)) {
				listEvidenceResDto.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
			}
		}
		return listEvidenceResDto;
	}

	@Override
	@Transactional
	public EvidenceAfterPost pushComment(Long evidenceAfterPostId, PostCommentReqDto postCommentRequestDto)
			throws BestWorkBussinessException {
		EvidenceAfterPost evidenceAfter = null;
		if (ObjectUtils.isNotEmpty(evidenceAfterPostId) && ObjectUtils.isNotEmpty(postCommentRequestDto)) {
			// Check exist post invoice with air way bill in DB
			evidenceAfter = this.evidenceAfterPostRepository.findByIdAndAirWayBill(evidenceAfterPostId,
					postCommentRequestDto.getAwbId());
			if (ObjectUtils.isEmpty(evidenceAfter)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eEA0002, null);
			}
			// Set comment
			evidenceAfter.setComment(postCommentRequestDto.getComment());
			this.evidenceAfterPostRepository.save(evidenceAfter);
		}
		return evidenceAfter;
	}

}