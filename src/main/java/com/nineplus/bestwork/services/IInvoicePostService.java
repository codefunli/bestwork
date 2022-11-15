package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.dto.PostInvoiceResDto;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface IInvoicePostService {
	PostInvoice savePostInvoice(PostInvoiceReqDto postInvoiceReqDto, String airWayBillCode)
			throws BestWorkBussinessException;

	Optional<PostInvoice> getPostInvoice(Long postInvoiceId) throws BestWorkBussinessException;

	void updatePostInvoice(List<MultipartFile> mFiles, PostInvoiceReqDto postInvoiceReqDto, String airWayCode)
			throws BestWorkBussinessException;

	public PostInvoiceResDto getDetailInvoice(Long invoicePostId) throws BestWorkBussinessException;

	List<PostInvoiceResDto> getAllInvoicePost(String airWayBillId) throws BestWorkBussinessException;

}
