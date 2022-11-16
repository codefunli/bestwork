package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface ICustomClearanceDocService {

	boolean downloadZip(String airWayBillCode, List<Long> ids) throws BestWorkBussinessException;
}
