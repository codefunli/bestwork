package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.AirWayBillAttachReqDto;
import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IAirWayBillService {
	void saveAirWayBill(AirWayBillReqDto airWayBillReqDto) throws BestWorkBussinessException;

	void update(List<MultipartFile> mFiles, PostInvoiceReqDto postInvoiceReqDto, String airWayCode)
			throws BestWorkBussinessException;
}
