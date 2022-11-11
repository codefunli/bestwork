package com.nineplus.bestwork.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.dto.PostInvoiceResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IPostInvoiceService;
import com.nineplus.bestwork.utils.CommonConstants;

@RestController
@RequestMapping("/api/v1/invoices")
public class PostInvoiceController extends BaseController {

	@Autowired
	IPostInvoiceService iPostInvoiceService;

	@PatchMapping("/update-invoice/{airWayBillCode}")
	public ResponseEntity<? extends Object> update(@RequestParam("file") List<MultipartFile> mFiles,
			@RequestParam("invoices-description") String invoiceDes,
			@RequestParam("invoices-comment") String invoiceCom, @PathVariable String airWayBillCode)
			throws BestWorkBussinessException {
		try {
			PostInvoiceReqDto postInvoiceReqDto = new PostInvoiceReqDto();
			if (StringUtils.isNotBlank(invoiceDes) || StringUtils.isNotBlank(invoiceCom)) {
				postInvoiceReqDto.setComment(invoiceCom);
				postInvoiceReqDto.setDescription(invoiceDes);
			}
			iPostInvoiceService.updatePostInvoice(mFiles, postInvoiceReqDto, airWayBillCode);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sI0001, null, null);
	}
	
	@GetMapping("/detail/{airWayBillCode}")
	public ResponseEntity<? extends Object> getDetalInvoice(@PathVariable String airWayBillCode)
			throws BestWorkBussinessException {
		PostInvoiceResDto postInvoiceResDto  = null;
		try {
			postInvoiceResDto = iPostInvoiceService.getDetailInvoice(airWayBillCode);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(postInvoiceResDto)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sI0002, postInvoiceResDto, null);
	}
	
}
