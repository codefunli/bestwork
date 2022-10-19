package com.nineplus.bestwork.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.ProjectRequestDto;
import com.nineplus.bestwork.dto.RProjectReqDto;
import com.nineplus.bestwork.dto.ProjectResponseDto;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IProjectService {

	public PageResponseDto<ProjectResponseDto> getProjectPage(RProjectReqDto pageSearchDto)
			throws BestWorkBussinessException;

	public PageResponseDto<ProjectResponseDto> getAllProjectPages(Pageable pageable) throws BestWorkBussinessException;

	public Optional<ProjectEntity> getProjectById(String id) throws BestWorkBussinessException;

	public ProjectEntity saveProject(@Valid ProjectRequestDto projectRequestDto) throws BestWorkBussinessException;

	public ProjectEntity updateProject(ProjectEntity project) throws BestWorkBussinessException;

}
