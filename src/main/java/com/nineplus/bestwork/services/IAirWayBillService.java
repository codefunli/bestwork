package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.AirWayBillAllInfoResDto;
import com.nineplus.bestwork.dto.AirWayBillAttachReqDto;
import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.entity.AirWayBill;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IAirWayBillService {
	void saveAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException;

	List<AirWayBill> getAllAirWayBill() throws BestWorkBussinessException;

	AirWayBillResDto getDetail(String code) throws BestWorkBussinessException;
}
