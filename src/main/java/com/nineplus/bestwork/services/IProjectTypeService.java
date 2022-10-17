package com.nineplus.bestwork.services;

import java.util.Optional;

import com.nineplus.bestwork.entity.TProjectType;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IProjectTypeService {

	Optional<TProjectType> getProjectTypeById(Integer projectTypeId) throws BestWorkBussinessException;

}
