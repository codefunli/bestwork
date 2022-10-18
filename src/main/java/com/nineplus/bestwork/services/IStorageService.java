package com.nineplus.bestwork.services;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author DiepTT
 *
 */

public interface IStorageService {
	// For save file
	public FileStorageEntity storeFile(String image, PostEntity reqpost);

	public Stream<Path> loadAll();

	// For read file
	public byte[] readFileContent(String filename);

	public void deleteFile(String fileName);
	

	public List<FileStorageEntity> getFilesByPostId(String postId) throws BestWorkBussinessException;

}
