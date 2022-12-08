package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.entity.NationEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface INationService {

	NationEntity findById(long id) throws BestWorkBussinessException;

	List<NationEntity> findAll() throws BestWorkBussinessException;

	List<NationEntity> findByIds(List<Long> nationIds);

}
