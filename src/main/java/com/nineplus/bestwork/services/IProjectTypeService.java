package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Optional;

import com.nineplus.bestwork.dto.ProjectTypeResponseDto;
import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IProjectTypeService {

	ProjectTypeEntity getProjectTypeById(Integer projectTypeId) throws BestWorkBussinessException;

	List<ProjectTypeResponseDto> getAllProjectTypes();

}
