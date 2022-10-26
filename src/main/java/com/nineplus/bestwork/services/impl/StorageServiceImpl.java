package com.nineplus.bestwork.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.entity.Progress;
import com.nineplus.bestwork.repository.StorageRepository;
import com.nineplus.bestwork.services.IStorageService;

/**
 * 
 * @author DiepTT
 *
 */

@Service
public class StorageServiceImpl implements IStorageService {

	@Autowired
	private StorageRepository storageRepository;

	@Override
	public FileStorageEntity storeFilePost(String imageData, PostEntity reqPost) {
		try {
			FileStorageEntity image = new FileStorageEntity();
			image.setData(imageData.getBytes());
			image.setPost(reqPost);
			String imageName = getImageName(reqPost);
			image.setName(imageName);
			String type = getImageType(imageData);
			image.setType(type);
			image.setCreateDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))));

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
	public FileStorageEntity storeFile(String imageData, Progress progress) {
		try {
			FileStorageEntity image = new FileStorageEntity();
			image.setData(imageData.getBytes());
			image.setProgress(progress);
			String generatedFileName = UUID.randomUUID().toString().replace("-", "");
			image.setName(generatedFileName);
			String type = getImageType(imageData);
			image.setType(type);
			image.setCreateDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))));

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
	public void deleteFilesByPostId(String postId) {
		this.storageRepository.deleteByPostId(postId);
	}
}
