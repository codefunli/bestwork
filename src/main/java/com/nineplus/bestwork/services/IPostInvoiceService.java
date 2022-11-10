package com.nineplus.bestwork.services;


import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IPostInvoiceService {
	PostInvoice savePostInvoice(PostInvoiceReqDto postInvoiceReqDto) throws BestWorkBussinessException;

}
