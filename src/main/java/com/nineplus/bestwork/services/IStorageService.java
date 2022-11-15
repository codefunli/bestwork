package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.FileStorageReqDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.entity.PostEntity;
import com.nineplus.bestwork.entity.ProgressEntity;
import com.nineplus.bestwork.utils.Enums.FolderType;

/**
 * 
 * @author DiepTT
 *
 */

public interface IStorageService {
	public FileStorageEntity storeFilePost(String image, PostEntity reqpost);

	public FileStorageEntity storeFileProgress(FileStorageReqDto files, ProgressEntity progress);

	public List<FileStorageEntity> findFilesByPostId(String postId);

	public List<FileStorageEntity> findFilesByProgressId(Long progressId);

	public void deleteFilesByPostId(String postId);
	
	public void storeFile(Long Id, FolderType type, String pathOnServer);

}
