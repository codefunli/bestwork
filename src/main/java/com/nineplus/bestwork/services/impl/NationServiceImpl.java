package com.nineplus.bestwork.services.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.NationEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.NationRepository;
import com.nineplus.bestwork.services.INationService;
import com.nineplus.bestwork.utils.CommonConstants;

@Service
public class NationServiceImpl implements INationService {
	@Autowired
	NationRepository nationRepository;

	@Override
	public NationEntity findById(long nationId) throws BestWorkBussinessException {
		Optional<NationEntity> nationOpt = this.nationRepository.findById(nationId);
		if (nationOpt.isEmpty()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ENA0001, null);
		}
		return nationOpt.get();
	}

	@Override
	public List<NationEntity> findAll() throws BestWorkBussinessException {
		List<NationEntity> nationList = this.nationRepository.findAll();
		if (ObjectUtils.isEmpty(nationList)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
		return nationList;
	}

	@Override
	public List<NationEntity> findByIds(List<Long> nationIds) {
		return this.nationRepository.findByIdIn(nationIds);
	}

}
