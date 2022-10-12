package com.nineplus.bestwork.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.entity.TFileStorage;

public interface IStorageService {
	// For save file
	public TFileStorage storeFile(MultipartFile file);

	public Stream<Path> loadAll();

	// For read file
	public byte[] readFileContent(String filename);

	public void deleteFile(String fileName);

}
