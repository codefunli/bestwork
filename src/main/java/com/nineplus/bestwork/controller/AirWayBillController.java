package com.nineplus.bestwork.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.AirWayBillStatusResDto;
import com.nineplus.bestwork.entity.AirWayBill;
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

	@GetMapping("/list")
	public ResponseEntity<? extends Object> getAllAirWayBill() throws BestWorkBussinessException {
		List<AirWayBill> listAwb = null;
		try {
			listAwb = iAirWayBillService.getAllAirWayBill();
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(listAwb)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sA0003, listAwb, null);
	}

	@GetMapping("{code}/detail")
	public ResponseEntity<? extends Object> getDetailAirWayBill(@PathVariable String code)
			throws BestWorkBussinessException {
		AirWayBillResDto airWayBillInfo = null;
		try {
			airWayBillInfo = iAirWayBillService.getDetail(code);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}

		if (ObjectUtils.isEmpty(airWayBillInfo)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sA0004, airWayBillInfo, null);

	}
}
