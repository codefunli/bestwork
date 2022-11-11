package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.FileStorageReqDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.entity.ProgressEntity;
import com.nineplus.bestwork.repository.StorageRepository;
import com.nineplus.bestwork.services.IStorageService;

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
			image.setCreateDate(LocalDateTime.now());

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
			image.setCreateDate(LocalDateTime.now());

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
	public void storeFilePostInvoice(Long postInvoiceId, String pathOnServer) {
		try {
			FileStorageEntity file = new FileStorageEntity();
			file.setPostInvoiceId(postInvoiceId);
			file.setPathFileServer(pathOnServer);
			file.setName(getFileNameFromPath(pathOnServer));
			file.setType(getFileTypeFromPath(pathOnServer));
			file.setCreateDate(LocalDateTime.now());
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
}
