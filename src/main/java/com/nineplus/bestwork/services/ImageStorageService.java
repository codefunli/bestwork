package com.nineplus.bestwork.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
//public class ImageStorageService implements IStorageService {
	public class ImageStorageService {
	
	private final Path storageFolder = Paths.get("uploads");
	
	private boolean isImageFile(MultipartFile file) {
        String fileExtension = file.getOriginalFilename().split("\\.")[1];
        String fileExtensions = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg","jpeg", "bmp"}).contains(fileExtensions.trim().toLowerCase());
    }

//	@Override
	public String storeFile(MultipartFile file) {
		try {
            if(file.isEmpty()) {
                throw new RuntimeException("Failed to storage empty file");
            }
            // check file is an image ?
            if(!isImageFile(file)) {
                throw new RuntimeException("You can only upload image");
            }

            //file must be < 5MB
            float fileSizeInMegabytes = file.getSize()/1_000_000.0f;
            if(fileSizeInMegabytes >= 5.0f) {
                throw new RuntimeException("File must be <= 5MB");
            }

            //File must be renamed
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName =  generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();

            if(!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Can not store file outside this directory");
            }
            try(InputStream  inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return generatedFileName;

        } catch (Exception exception) {
            throw new RuntimeException("Failed to storage empty file", exception);
        }
	}

//	@Override
	public Stream<Path> loadAll() {
		try {
            return Files.walk(this.storageFolder,1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("_."))
                    .map(this.storageFolder::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to storage empty file", e);
        }
	}

//	@Override
	public byte[] readFileContent(String filename) {
		try {
            Path file = storageFolder.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException("Cound not read file" + filename);

            }
        } catch (IOException exception) {
            throw new RuntimeException("Cound not read file" + filename, exception);
        }
	}

//	@Override
	public void deleteFile(String fileName) {
		
	}

}
