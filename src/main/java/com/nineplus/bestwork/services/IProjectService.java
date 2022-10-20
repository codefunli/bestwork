package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.ProjectRequestDto;
import com.nineplus.bestwork.dto.ProjectResponseDto;
import com.nineplus.bestwork.dto.RProjectReqDto;
import com.nineplus.bestwork.entity.ProjectEntity;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IProjectService {

	public PageResponseDto<ProjectResponseDto> getProjectPage(PageSearchDto pageSearchDto)
			throws BestWorkBussinessException;

	public PageResponseDto<ProjectResponseDto> getAllProjectPages(Pageable pageable) throws BestWorkBussinessException;

	public Optional<ProjectEntity> getProjectById(String id) throws BestWorkBussinessException;

	public ProjectEntity saveProject(@Valid ProjectRequestDto projectRequestDto, ProjectTypeEntity projectType) throws BestWorkBussinessException;

	public ProjectEntity updateProject(ProjectEntity project) throws BestWorkBussinessException;

	public void deleteProjectById(List<String> list) throws BestWorkBussinessException;

}
