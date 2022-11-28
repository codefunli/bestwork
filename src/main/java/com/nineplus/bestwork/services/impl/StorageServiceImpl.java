package com.nineplus.bestwork.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.ChangeStatusFileDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.StorageRepository;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.FolderType;

/**
 * 
 * @author DiepTT
 *
 */

@Service
@Transactional
public class StorageServiceImpl implements IStorageService {

	@Autowired
	private StorageRepository storageRepository;

	@Override
	@Transactional
	public FileStorageEntity storeFilePost(String imageData, PostEntity reqPost) {
		try {
			FileStorageEntity image = new FileStorageEntity();
			image.setData(imageData.getBytes());
			image.setPost(reqPost);
			String imageName = getImageName(reqPost);
			image.setName(imageName);
			String type = getImageType(imageData);
			image.setType(type);
			image.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

			return storageRepository.save(image);
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	private String getImageType(String imageData) {
		String prefixRegex = "data:image/";
		String suffixRegex = ";base64";
		Pattern pattern = Pattern.compile(prefixRegex + "(.*?)" + suffixRegex);
		Matcher matcher = pattern.matcher(imageData);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private String getImageName(PostEntity reqPost) {
//		String projectName = reqPost.getProject().getProjectName();
		String constructionName = reqPost.getConstruction().getConstructionName();
		String description = reqPost.getDescription();
		String imageName = constructionName + ": " + description;
		if (imageName.length() <= 40) {
			return imageName;
		} else {
			return imageName.substring(0, 30) + "...";
		}

	}

	public List<FileStorageEntity> findFilesByPostId(String postId) {
		return this.storageRepository.findAllByPostId(postId);
	}

	@Override
	public List<FileStorageEntity> findFilesByProgressId(Long progressId) {
		return this.storageRepository.findAllByProgressId(progressId);
	}

	@Override
	public void deleteFilesByPostId(String postId) {
		this.storageRepository.deleteByPostId(postId);
	}

	@Override
	@Transactional
	public void storeFile(Long id, FolderType type, String pathOnServer) {
		try {
			FileStorageEntity file = new FileStorageEntity();
			switch (type) {
			case INVOICE:
				file.setPostInvoiceId(id);
				break;
			case PACKAGE:
				file.setPackagePostId(id);
				break;
			case EVIDENCE_BEFORE:
				file.setEvidenceBeforePostId(id);
				break;
			case EVIDENCE_AFTER:
				file.setEvidenceAfterPostId(id);
				break;
			case CONSTRUCTION:
				file.setConstructionId(id);
				break;
			case PROGRESS:
				file.setProgressId(id);
				break;
			default:
				break;
			}
			file.setPathFileServer(pathOnServer);
			file.setName(getFileNameFromPath(pathOnServer));
			file.setType(getFileTypeFromPath(pathOnServer));
			file.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
			storageRepository.save(file);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private String getFileNameFromPath(String path) {
		String name = FilenameUtils.getName(path);
		if (name.length() >= CommonConstants.Image.IMG_NAME_LEN) {
			name = name.substring(0, CommonConstants.Image.IMG_NAME_LEN - 1);
		}
		return name;
	}

	private String getFileTypeFromPath(String path) {
		return FilenameUtils.getExtension(path);
	}

	@Override
	public void changeStatusFile(ChangeStatusFileDto changeStatusFileDto) throws BestWorkBussinessException {
		if (ObjectUtils.isNotEmpty(changeStatusFileDto)) {
			String postType = changeStatusFileDto.getPostType();
			long postId = changeStatusFileDto.getPostId();
			boolean toStatus = changeStatusFileDto.isDestinationStatus();
			Long[] fileId = changeStatusFileDto.getFileId();
			List<Long> listFile = Arrays.asList(fileId);
			if (CommonConstants.Character.TYPE_POST_INVOICE.equals(postType)) {
				storageRepository.changeStatusInvoice(postId, listFile, toStatus);
			} else if (CommonConstants.Character.TYPE_POST_PACKAGE.equals(postType)) {
				storageRepository.changeStatusPackage(postId, listFile, toStatus);
			}
		}
	}

	@Override
	public Map<Long, String> getPathFileToDownLoad(String airWayBillCode, List<Long> listFileId)
			throws BestWorkBussinessException {
		Map<Long, String> mapPathFile = new HashMap<>();
		List<FileStorageEntity> listFile = storageRepository.findAllById(listFileId);
		if (ObjectUtils.isNotEmpty(listFile)) {
			for (FileStorageEntity file : listFile) {
				mapPathFile.put(file.getId(), file.getPathFileServer());
			}
		}
		return null;
	}

	@Override
	public List<String> getPathFileByCstrtId(long constructionId) {
		List<String> pathList = this.storageRepository.findAllPathsByCstrtId(constructionId);
		return pathList;
	}

	@Override
	public List<String> getPathFileByProgressId(long progressId) {
		List<String> pathList = this.storageRepository.findAllPathsByProgressId(progressId);
		return pathList;
	}

	@Override
	public void deleteByCstrtId(long constructionId) {
		this.storageRepository.deleteByConstructionId(constructionId);
	}

	@Override
	public void deleteByProgressId(long progressId) {
		this.storageRepository.deleteByProgressId(progressId);
	}
}
