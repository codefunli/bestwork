package com.nineplus.bestwork.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.repository.AirWayBillRepository;
import com.nineplus.bestwork.services.IAirWayBillService;

@Service
@Transactional
public class AirWayBillServiceImpl implements IAirWayBillService {
	@Autowired
	private AirWayBillRepository airWayBillRepository;

	@Override
	public AirWayBill findByCode(String code) {
		return this.airWayBillRepository.findByCode(code);
	}

}
