package com.nineplus.bestwork.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.TProjectType;
import com.nineplus.bestwork.repository.ProjectTypeRepository;
import com.nineplus.bestwork.services.IProjectTypeService;

@Service
public class ProjectTypeServiceImpl implements IProjectTypeService {

	@Autowired
	private ProjectTypeRepository projectTypeRepository;

	@Override
	public Optional<TProjectType> getProjectTypeById(Integer projectTypeId) {
		return this.projectTypeRepository.findById(projectTypeId);
	}

}
