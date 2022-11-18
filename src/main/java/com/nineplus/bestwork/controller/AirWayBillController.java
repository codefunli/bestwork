package com.nineplus.bestwork.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.nineplus.bestwork.dto.AirWayBillReqDto;
import com.nineplus.bestwork.dto.AirWayBillResDto;
import com.nineplus.bestwork.dto.AirWayBillStatusReqDto;
import com.nineplus.bestwork.dto.AirWayBillStatusResDto;
import com.nineplus.bestwork.dto.ChangeStatusFileDto;
import com.nineplus.bestwork.dto.CustomClearanceResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IAirWayBillService;
import com.nineplus.bestwork.services.IStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.AirWayBillStatus;

@RestController
@RequestMapping("/api/v1/airway-bill")
public class AirWayBillController extends BaseController {

	@Autowired
	IAirWayBillService iAirWayBillService;

	@Autowired
	IStorageService iStorageService;

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

	@GetMapping("/list/by/{projectId}")
	public ResponseEntity<? extends Object> getAllAirWayBill(@PathVariable String projectId)
			throws BestWorkBussinessException {
		List<AirWayBillResDto> listAwb = null;
		try {
			listAwb = iAirWayBillService.getAllAirWayBillByProject(projectId);
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

	@PostMapping("/change-status-file")
	public ResponseEntity<? extends Object> changeStatus(@RequestBody ChangeStatusFileDto changeStatusFileDto)
			throws BestWorkBussinessException {
		try {
			iStorageService.changeStatusFile(changeStatusFileDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sF0003, null, null);
	}

	@GetMapping("{code}/get-custom-clearance-doc")
	public ResponseEntity<? extends Object> getCustomClearanceDoc(@PathVariable String code)
			throws BestWorkBussinessException {
		CustomClearanceResDto customClearanceResDto = null;
		try {
			customClearanceResDto = iAirWayBillService.getCustomClearanceDoc(code);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}

		if (ObjectUtils.isEmpty(customClearanceResDto.getInvoicesDoc())
				&& ObjectUtils.isEmpty(customClearanceResDto.getPackagesDoc())) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sA0005, customClearanceResDto, null);
	}

	
	@PostMapping("{code}/change-status")
	public ResponseEntity<? extends Object> confirmDone(@PathVariable String code, @RequestBody AirWayBillStatusReqDto statusDto )
			throws BestWorkBussinessException {
		try {
			if(ObjectUtils.isNotEmpty(statusDto.getDestinationStatus())) {
			iAirWayBillService.confirmDone(code, statusDto.getDestinationStatus());
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sA0006, null, null);
	}

	@GetMapping("{airWayBillCode}/download-clearance-doc")
	public ResponseEntity<StreamingResponseBody> downloadZip(HttpServletResponse response,
			@PathVariable String airWayBillCode) throws BestWorkBussinessException {
		StreamingResponseBody streamResponseBody = iAirWayBillService.downloadZip(airWayBillCode, response);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=example.zip");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");

		return ResponseEntity.ok(streamResponseBody);
	}
}
