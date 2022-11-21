package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.ProgressAndConstructionResDto;
import com.nineplus.bestwork.dto.ProgressReqDto;
import com.nineplus.bestwork.dto.ProgressResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author TuanNA
 *
 */
public interface IProgressService {
	void registProgress(ProgressReqDto progressReqDto) throws BestWorkBussinessException;

	void updateProgress(ProgressReqDto progressReqDto, Long progressId) throws BestWorkBussinessException;

	List<ProgressResDto> getProgressByConstructionId(Long cstrtId) throws BestWorkBussinessException;

//	ProgressAndProjectResDto getProjectAndProgress(String projectId) throws BestWorkBussinessException;

	void deleteProgressList(List<Long> ids) throws BestWorkBussinessException;

	ProgressResDto getProgressById(Long progressId) throws BestWorkBussinessException;

	List<Long> getAllProgressByProject(List<String> listProjectId) throws BestWorkBussinessException;

	ProgressAndConstructionResDto getProgressByConstruction(String constructionId) throws BestWorkBussinessException;
}