package com.nineplus.bestwork.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.ProjectTypeResponseDto;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.repository.ProjectTypeRepository;
import com.nineplus.bestwork.services.IProjectTypeService;

@Service
public class ProjectTypeServiceImpl implements IProjectTypeService {

	@Autowired
	private ProjectTypeRepository projectTypeRepository;

	@Override
	public ProjectTypeEntity getProjectTypeById(Integer projectTypeId) {
		Optional<ProjectTypeEntity> typeOptional = this.projectTypeRepository.findById(projectTypeId);
		if (!typeOptional.isPresent()) {
			return null;
		}
		return typeOptional.get();
	}

	@Override
	public List<ProjectTypeResponseDto> getAllProjectTypes() {
		List<ProjectTypeEntity> typeList = this.projectTypeRepository.findAll();
		List<ProjectTypeResponseDto> projectTypeResponseDtos = new ArrayList<>();
		for (ProjectTypeEntity type : typeList) {
			ProjectTypeResponseDto dto = new ProjectTypeResponseDto();
			dto.setId(type.getId());
			dto.setName(type.getName());
			dto.setDescription(type.getDescription());

			projectTypeResponseDtos.add(dto);
		}
		return projectTypeResponseDtos;
	}

}
