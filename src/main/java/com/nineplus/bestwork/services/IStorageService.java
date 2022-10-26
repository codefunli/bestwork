package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.entity.Progress;

/**
 * 
 * @author DiepTT
 *
 */

public interface IStorageService {
	public FileStorageEntity storeFilePost(String image, PostEntity reqpost);
	public FileStorageEntity storeFile(String image, Progress progress);
	public List<FileStorageEntity> findFilesByPostId(String postId);
	public void deleteFilesByPostId(String postId);
}
