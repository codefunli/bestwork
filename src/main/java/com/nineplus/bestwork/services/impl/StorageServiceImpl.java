package com.nineplus.bestwork.services.impl;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.entity.TFileStorage;
import com.nineplus.bestwork.repository.StorageRepository;
import com.nineplus.bestwork.services.IStorageService;
import org.springframework.util.StringUtils;

@Service
public class StorageServiceImpl implements IStorageService {

	@Autowired
	private StorageRepository storageRepository;

	@Override
	public TFileStorage storeFile(MultipartFile file) {

		try {
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
//			String id = "F";
			String newFileName = StringUtils.cleanPath(file.getOriginalFilename());

//			String newFileName = simpleDateFormat.format(new Date()) + file.getOriginalFilename();
//			byte[] bytes = file.getBytes();
//			String type = file.getContentType();
//			Timestamp createDate = new Timestamp(System.currentTimeMillis());
//			TFileStorage fileStorage = new TFileStorage(newFileName, bytes, type, createDate);
			TFileStorage fileStorage = new TFileStorage(newFileName, file.getBytes(), file.getContentType());
			return this.storageRepository.save(fileStorage);
//			 "Upload file successfully!";

		} catch (Exception e) {
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

}
