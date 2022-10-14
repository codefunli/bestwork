package com.nineplus.bestwork.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import com.nineplus.bestwork.dto.PageResponseDTO;
import com.nineplus.bestwork.dto.ProjectRequestDto;
import com.nineplus.bestwork.dto.RProjectReqDTO;
import com.nineplus.bestwork.dto.TProjectResponseDto;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IProjectService {

	public PageResponseDTO<TProjectResponseDto> getProjectPage(RProjectReqDTO pageSearchDto)
			throws BestWorkBussinessException;

	public PageResponseDTO<TProjectResponseDto> getAllProjectPages(Pageable pageable) throws BestWorkBussinessException;

	public Optional<TProject> getProjectById(String id) throws BestWorkBussinessException;

	public TProject saveProject(@Valid ProjectRequestDto projectRequestDto) throws BestWorkBussinessException;

	public TProject updateProject(TProject project) throws BestWorkBussinessException;

}
