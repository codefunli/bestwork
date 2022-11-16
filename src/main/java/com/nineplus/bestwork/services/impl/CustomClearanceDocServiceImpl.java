package com.nineplus.bestwork.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.ICustomClearanceDocService;

@Service
public class CustomClearanceDocServiceImpl implements ICustomClearanceDocService {

	@Override
	public boolean downloadZip(String airWayBillCode, List<Long> ids) throws BestWorkBussinessException {
		return true;
	}

}
