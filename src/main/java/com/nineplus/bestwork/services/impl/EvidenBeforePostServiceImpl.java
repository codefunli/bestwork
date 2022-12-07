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

import com.nineplus.bestwork.dto.CustomClearanceImageFileResDto;
import com.nineplus.bestwork.dto.EvidenceBeforeReqDto;
import com.nineplus.bestwork.dto.EvidenceBeforeResDto;
import com.nineplus.bestwork.dto.FileStorageResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.entity.EvidenceBeforePost;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.EvidenceBeforePostRepository;
import com.nineplus.bestwork.repository.ImageBeforeFileProjection;
import com.nineplus.bestwork.services.IEvidenBeforePostService;
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
public class EvidenBeforePostServiceImpl implements IEvidenBeforePostService {

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	ISftpFileService sftpFileService;

	@Autowired
	IStorageService iStorageService;

	@Autowired
	EvidenceBeforePostRepository evidenceBeforePostRepository;

	@Override
	@Transactional
	public void updateEvidenceBefore(EvidenceBeforeReqDto evidenceBeforeReqDto, List<MultipartFile> mFiles)
			throws BestWorkBussinessException {
		EvidenceBeforePost evidenceBefore = null;
		// Validate file
		if (!sftpFileService.isValidFile(mFiles)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0002, null);
		}
		if (!sftpFileService.isImageFile(mFiles)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eF0004, null);
		}
		try {
			if (ObjectUtils.isNotEmpty(evidenceBeforeReqDto)) {
				long awbId = evidenceBeforeReqDto.getAwbId();
				// Save information for post invoice
				evidenceBefore = this.saveEvidenceBefore(evidenceBeforeReqDto);
				long evidenceBeforePostId = evidenceBefore.getId();
				// Upload file of post invoice into sever
				for (MultipartFile mFile : mFiles) {
					String pathServer = sftpFileService.uploadEvidenceBefore(mFile, awbId, evidenceBeforePostId);
					// Save path file of post invoice
					iStorageService.storeFile(evidenceBeforePostId, FolderType.EVIDENCE_BEFORE, pathServer);
				}
			}
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eEB0001, null);
		}
	}

	@Transactional
	private EvidenceBeforePost saveEvidenceBefore(EvidenceBeforeReqDto evidenceBeforeReqDto)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		EvidenceBeforePost evidenceBefore = new EvidenceBeforePost();
		try {
			evidenceBefore.setAirWayBill(evidenceBeforeReqDto.getAwbId());
			evidenceBefore.setDescription(evidenceBeforeReqDto.getDescription());
			evidenceBefore.setCreateBy(userAuthRoleReq.getUsername());
			evidenceBefore.setUpdateBy(userAuthRoleReq.getUsername());
			evidenceBefore.setCreateDate(LocalDateTime.now());
			evidenceBefore.setUpdateDate(LocalDateTime.now());
			return evidenceBeforePostRepository.save(evidenceBefore);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0001, null);
		}
	}

	@Override
	public List<EvidenceBeforeResDto> getAllEvidenceBefore(long awbId) throws BestWorkBussinessException {
		List<EvidenceBeforePost> listEvidence = evidenceBeforePostRepository.findByAirWayBill(awbId);
		List<EvidenceBeforeResDto> listEvidenceResDto = new ArrayList<>();
		EvidenceBeforeResDto res = null;
		for (EvidenceBeforePost evidence : listEvidence) {
			res = new EvidenceBeforeResDto();
			res.setId(evidence.getId());
			res.setComment(evidence.getComment());
			res.setDescription(evidence.getDescription());
			res.setCreateBy(evidence.getCreateBy());
			res.setUpdateBy(evidence.getUpdateBy());
			res.setCreateDate(evidence.getCreateDate().toString());
			res.setUpdateDate(evidence.getUpdateDate().toString());
			res.setPostType(CommonConstants.Character.TYPE_POST_IMAGE_BEFORE);
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
	public EvidenceBeforePost pushComment(Long evidenceBeforePostId, PostCommentReqDto postCommentRequestDto)
			throws BestWorkBussinessException {
		EvidenceBeforePost evidenceBefore = null;
		try {
			if (ObjectUtils.isNotEmpty(evidenceBeforePostId) && ObjectUtils.isNotEmpty(postCommentRequestDto)) {
				// Check exist post invoice with air way bill in DB
				evidenceBefore = this.evidenceBeforePostRepository.findByIdAndAirWayBill(evidenceBeforePostId,
						postCommentRequestDto.getAwbId());
				if (ObjectUtils.isEmpty(evidenceBefore)) {
					throw new BestWorkBussinessException(CommonConstants.MessageCode.eEB0002, null);
				}
				// Set comment
				evidenceBefore.setComment(postCommentRequestDto.getComment());
				this.evidenceBeforePostRepository.save(evidenceBefore);
			}
		} catch (Exception e) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eEB0003, null);
		}
		return evidenceBefore;
	}

	@Override
	public List<CustomClearanceImageFileResDto> getImageClearance(long awbId) throws BestWorkBussinessException {
		List<CustomClearanceImageFileResDto> lst = new ArrayList<>();
		CustomClearanceImageFileResDto customClearanceImageFileResDto = null;
		List<ImageBeforeFileProjection> res = evidenceBeforePostRepository.getClearanceImageInfo(awbId);
		for (ImageBeforeFileProjection projection : res) {
			customClearanceImageFileResDto = new CustomClearanceImageFileResDto();
			customClearanceImageFileResDto.setFileId(projection.getFileId());
			customClearanceImageFileResDto.setPostImageBeforeId(projection.getPostImageBeforeId());
			customClearanceImageFileResDto.setName(projection.getName());
			customClearanceImageFileResDto.setType(projection.getType());
			customClearanceImageFileResDto.setPostType(CommonConstants.Character.TYPE_POST_IMAGE_BEFORE);
			// return content file if file is image
			if (Arrays.asList(CommonConstants.Image.IMAGE_EXTENSION).contains(projection.getType())) {
				String pathServer = projection.getPathFileServer();
				byte[] imageContent = sftpFileService.getFile(pathServer);
				customClearanceImageFileResDto.setContent(imageContent);
			}
			lst.add(customClearanceImageFileResDto);
		}
		return lst;
	}
}