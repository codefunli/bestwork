package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;

/**
 * 
 * @author DiepTT
 *
 */

public interface IStorageService {
	public FileStorageEntity storeFile(String image, PostEntity reqpost);

	public List<FileStorageEntity> findFilesByPostId(String postId);

	public void deleteFilesByPostId(String postId);
}
