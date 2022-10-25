package com.nineplus.bestwork.services;

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.entity.Progress;

/**
 * 
 * @author DiepTT
 *
 */

public interface IStorageService {
	public FileStorageEntity storeFile(String image, PostEntity reqpost);
	public FileStorageEntity storeFile(String image, Progress progress);
}
