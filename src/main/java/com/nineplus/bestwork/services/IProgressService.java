package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.ProgressReqDto;
import com.nineplus.bestwork.dto.ProgressResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author TuanNA
 *
 */
public interface IProgressService {
	void registProgress(ProgressReqDto progressReqDto, List<MultipartFile> fileBefore, List<MultipartFile> fileAfter) throws BestWorkBussinessException;

	void updateProgress(ProgressReqDto progressReqDto, List<MultipartFile> fileBefore, List<MultipartFile> fileAfter, Long progressId)
			throws BestWorkBussinessException;

	void deleteProgressList(List<Long> ids) throws BestWorkBussinessException;

	//ProgressResDto getProgressById(Long progressId) throws BestWorkBussinessException;

	List<ProgressResDto> getProgressByConstruction(String constructionId) throws BestWorkBussinessException;

}