package com.nineplus.bestwork.services;

import java.util.Optional;

import com.nineplus.bestwork.entity.ProjectTypeEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IProjectTypeService {

	Optional<ProjectTypeEntity> getProjectTypeById(Integer projectTypeId) throws BestWorkBussinessException;

}
