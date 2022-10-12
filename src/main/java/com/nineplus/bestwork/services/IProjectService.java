package com.nineplus.bestwork.services;

import org.springframework.data.domain.Pageable;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IProjectService {

	public PageResponseDTO<TProjectResponseDto> getProjectPage(RProjectReqDTO pageSearchDto) throws BestWorkBussinessException;
	public PageResponseDTO<TProjectResponseDto> getAllProjectPages(Pageable pageable) throws BestWorkBussinessException;
	
}
