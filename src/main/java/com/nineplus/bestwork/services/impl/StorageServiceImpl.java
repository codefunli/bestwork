package com.nineplus.bestwork.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
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
	public FileStorageEntity storeFile(String file, PostEntity reqPost) {

		try {
			FileStorageEntity image = new FileStorageEntity();
			image.setData(file.getBytes());
			String description = reqPost.getDescription();
			if (description.length() < 50) {
				image.setName(reqPost.getProject().getProjectName() + ": " + reqPost.getDescription());
			} else {
				image.setName(reqPost.getProject().getProjectName() + ": " + reqPost.getDescription().substring(0, 50)
						+ "...");
			}
			image.setPost(reqPost);
			String type = "";
			Pattern pattern = Pattern.compile("data:image/(.*?);base64");
			Matcher matcher = pattern.matcher(file);
			if (matcher.find()) {
				System.out.println(matcher.group(1));
				type = matcher.group(1);
			}

			image.setType(type);
			image.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

			return storageRepository.save(image);
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

}
