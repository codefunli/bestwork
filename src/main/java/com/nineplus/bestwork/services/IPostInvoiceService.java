package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.AirWayBillAllInfoResDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.dto.PostInvoiceResDto;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IPostInvoiceService {
	PostInvoice savePostInvoice(PostInvoiceReqDto postInvoiceReqDto, String airWayBillCode)
			throws BestWorkBussinessException;

	PostInvoice getPostInvoice(String airWayBillcode) throws BestWorkBussinessException;

	void updatePostInvoice(List<MultipartFile> mFiles, PostInvoiceReqDto postInvoiceReqDto, String airWayCode)
			throws BestWorkBussinessException;

	public PostInvoiceResDto getDetailInvoice(String airWayBillCode) throws BestWorkBussinessException;

}
