package com.nineplus.bestwork.services.impl;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

//			LocalDateTime currentLocalDateTime = LocalDateTime.now();
//			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
//			String formattedDateTime = currentLocalDateTime.format(dateTimeFormatter);
//			String newFileName = formattedDateTime + "-" + StringUtils.cleanPath(file.getOriginalFilename());
//
//			Timestamp createDate = new Timestamp(System.currentTimeMillis());
//
//			return this.storageRepository
//					.save(new FileStorageEntity(newFileName, file.getBytes(), file.getContentType(), createDate, reqPost));

			FileStorageEntity image = new FileStorageEntity();
			image.setData(file.getBytes());
			image.setName("abc");
			image.setPost(reqPost);

			String typeString = "";
			if (file.substring(0, 20).contains("png")) {
				typeString = "png";
			} else if (file.substring(0, 20).contains("jpg")) {
				typeString = "jpg";
			} else if (file.substring(0, 20).contains("gif")) {
				typeString = "gif";
			}
			image.setType(typeString);
			image.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

			return storageRepository.save(image);
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	@Override
	public Stream<Path> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] readFileContent(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(String fileName) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<FileStorageEntity> getFilesByPostId(String postId) {
		return this.storageRepository.findAllByPostId(postId);
	}


}
