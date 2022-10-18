package com.nineplus.bestwork.services;

import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;

/**
 * 
 * @author DiepTT
 *
 */

public interface IStorageService {
	public FileStorageEntity storeFile(String image, PostEntity reqpost);
}
