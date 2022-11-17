package com.nineplus.bestwork.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.ChangeStatusFileDto;
import com.nineplus.bestwork.dto.FileStorageReqDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.entity.ProgressEntity;
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

	private final String POST_INVOICE_TYPE = "invoice";
	private final String POST_PACKAGE_TYPE = "package";

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
		String projectName = reqPost.getProject().getProjectName();
		String description = reqPost.getDescription();
		String imageName = projectName + ": " + description;
		if (imageName.length() <= 40) {
			return imageName;
		} else {
			return imageName.substring(0, 30) + "...";
		}

	}

	@Override
	@Transactional
	public FileStorageEntity storeFileProgress(FileStorageReqDto file, ProgressEntity progress) {
		try {
			FileStorageEntity image = new FileStorageEntity();
			image.setData(file.getData().getBytes());
			image.setProgress(progress);
			String generatedFileName = UUID.randomUUID().toString().replace("-", "");
			image.setName(generatedFileName);
			image.setType(getImageType(file.getData()));
			image.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

			return storageRepository.save(image);
		} catch (Exception e) {
			e.getMessage();
			return null;
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
	public void storeFile(Long Id, FolderType type, String pathOnServer) {
		try {
			FileStorageEntity file = new FileStorageEntity();
			switch (type) {
			case INVOICE:
				file.setPostInvoiceId(Id);
				break;
			case PACKAGE:
				file.setPackagePostId(Id);
				break;
			case EVIDENCE_BEFORE:
				// file.setPackagePostId(Id);
				break;
			case EVIDENCE_AFTER:
				// file.setPackagePostId(Id);
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
		return FilenameUtils.getName(path);
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
				storageRepository.changeStatusInvoice(postId, listFile, toStatus);
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
}
