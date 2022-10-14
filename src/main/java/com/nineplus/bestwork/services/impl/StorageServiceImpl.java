package com.nineplus.bestwork.services.impl;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.entity.TFileStorage;
import com.nineplus.bestwork.repository.StorageRepository;
import com.nineplus.bestwork.services.IStorageService;

@Service
public class StorageServiceImpl implements IStorageService {

	@Autowired
	private StorageRepository storageRepository;

	@Override
	public TFileStorage storeFile(MultipartFile file) {

		try {

			LocalDateTime currentLocalDateTime = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
			String formattedDateTime = currentLocalDateTime.format(dateTimeFormatter);
			String newFileName = formattedDateTime + "-" + StringUtils.cleanPath(file.getOriginalFilename());
			
			Timestamp createDate = new Timestamp(System.currentTimeMillis());

			return this.storageRepository.save(new TFileStorage(newFileName, file.getBytes(), file.getContentType(),
					createDate));

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
	public List<TFileStorage> getFilesByProjectId(String projectId) {
		return this.storageRepository.findAllByProjectId(projectId);
	}

}
