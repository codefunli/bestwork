package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Map;

import com.nineplus.bestwork.dto.ChangeStatusFileDto;
import com.nineplus.bestwork.entity.FileStorageEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.utils.Enums.FolderType;

/**
 * 
 * @author DiepTT
 *
 */

public interface IStorageService {

	public List<FileStorageEntity> findFilesByPostId(String postId);

	public List<FileStorageEntity> findFilesByProgressId(Long progressId);

	public void deleteFilesByPostId(String postId);

	public void storeFile(Long Id, FolderType type, String pathOnServer);

	public void changeStatusFile(ChangeStatusFileDto changeStatusFileDto) throws BestWorkBussinessException;

	Map<Long, String> getPathFileToDownLoad(String airWayBillCode, List<Long> listFileId)
			throws BestWorkBussinessException;

	public void deleteByCstrtId(long constructionId);

	public List<String> getPathFileByCstrtId(long constructionId);

	public List<String> getPathFileByProgressId(long id);

	public void deleteByProgressId(long id);

	public void deleteByProgressIds(List<Long> ids);

	public void deleteByCstrtIds(List<Long> ids);

}
