package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.ProgressReqDto;
import com.nineplus.bestwork.dto.ProgressResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author TuanNA
 *
 */
public interface IProgressService {
	void saveProgress(ProgressReqDto progressReqDto) throws BestWorkBussinessException;

	List<ProgressResDto> getAllProgress() throws BestWorkBussinessException;

	List<ProgressResDto> getProgressByProjectId(String projectId) throws BestWorkBussinessException;
}
