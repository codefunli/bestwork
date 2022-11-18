package com.nineplus.bestwork.services;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.CustomClearanceResDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IAirWayBillService {
	void saveAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException;

	List<AirWayBillResDto> getAllAirWayBillByProject(String projectId) throws BestWorkBussinessException;

	AirWayBillResDto getDetail(String code) throws BestWorkBussinessException;

	AirWayBill findByCode(String code);

	CustomClearanceResDto getCustomClearanceDoc(String code) throws BestWorkBussinessException;

	StreamingResponseBody downloadZip(String code, HttpServletResponse response) throws BestWorkBussinessException;

	void confirmDone(String code, int destinationStatus) throws BestWorkBussinessException;

}
