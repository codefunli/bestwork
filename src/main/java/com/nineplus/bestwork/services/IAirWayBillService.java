package com.nineplus.bestwork.services;

import com.nineplus.bestwork.entity.AirWayBill;

public interface IAirWayBillService {

	AirWayBill findByCode(String code);

}