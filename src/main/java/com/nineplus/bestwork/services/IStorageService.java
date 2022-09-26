package com.nineplus.bestwork.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
	// For save file
	public String storeFile(MultipartFile file);

	public Stream<Path> loadAll();

	// For read file
	public byte[] readFileContent(String filename);

	public void deleteFile(String fileName);

}
