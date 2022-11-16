package com.nineplus.bestwork.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.FileListDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.ICustomClearanceDocService;
import com.nineplus.bestwork.utils.CommonConstants;

@RestController
@RequestMapping("/api/v1/customs-clearance")
public class CustomClearanceDocController extends BaseController {
	
    @Autowired
    ICustomClearanceDocService iCustomClearanceDocService;

	@PostMapping("/download/by/{airWayBillCode}")
	public ResponseEntity<? extends Object> downloadFolder(@PathVariable String airWayBillCode, @RequestBody FileListDto fileListDto)
			throws BestWorkBussinessException {
		try {
			iCustomClearanceDocService.downloadZip(airWayBillCode, Arrays.asList(fileListDto.getListFileId()));
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sA0001, null, null);
	}

}