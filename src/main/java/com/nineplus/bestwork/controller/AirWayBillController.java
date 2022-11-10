package com.nineplus.bestwork.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.AirWayBillAttachReqDto;
import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillStatusResDto;
import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;

@RestController
@RequestMapping("/api/v1/airway-bill")
public class AirWayBillController extends BaseController {

	@Autowired
	IAirWayBillService iAirWayBillService;

	@PostMapping("/create")
	public ResponseEntity<? extends Object> create(@RequestBody AirWayBillReqDto airWayBillReqDto)
			throws BestWorkBussinessException {
		try {
			iAirWayBillService.saveAirWayBill(airWayBillReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sA0001, null, null);
	}

	@PatchMapping("/update-invoice/{airWayBillCode}")
	public ResponseEntity<? extends Object> update(@RequestParam("file") List<MultipartFile> mFiles,
			@RequestParam("invoices-description") String invoiceDes, @RequestParam("invoices-comment") String invoiceCom, @PathVariable String airWayBillCode)
			throws BestWorkBussinessException {
		try {
			PostInvoiceReqDto postInvoiceReqDto = new PostInvoiceReqDto();
			if(StringUtils.isNotBlank(invoiceDes) || StringUtils.isNotBlank(invoiceCom)) {
				postInvoiceReqDto.setComment(invoiceCom);
				postInvoiceReqDto.setDescription(invoiceDes);
			}
			iAirWayBillService.update(mFiles,postInvoiceReqDto, airWayBillCode);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sA0001, null, null);
	}
	
	@GetMapping("/status")
	public ResponseEntity<? extends Object> getAirWayBillStatus() throws BestWorkBussinessException {
		List<AirWayBillStatusResDto> airWayBillStatus = new ArrayList<>();
		for (AirWayBillStatus status : AirWayBillStatus.values()) {
			AirWayBillStatusResDto dto = new AirWayBillStatusResDto();
			dto.setId(status.ordinal());
			dto.setStatus(status.getValue());
			airWayBillStatus.add(dto);
		}
		return success(CommonConstants.MessageCode.sA0002, airWayBillStatus, null);
	}
}
