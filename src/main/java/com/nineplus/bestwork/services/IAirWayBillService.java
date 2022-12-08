package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.CustomClearanceResDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IAirWayBillService {
	void saveAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException;

	List<AirWayBillResDto> getAllAirWayBillByProject(String projectId) throws BestWorkBussinessException;

	AirWayBill findByCode(String code);

	CustomClearanceResDto getCustomClearanceDoc(long awbId) throws BestWorkBussinessException;

	List<String> createZipFolder(long awbId) throws BestWorkBussinessException;

	void changeStatus(long id, int destinationStatus) throws BestWorkBussinessException;
	
}
