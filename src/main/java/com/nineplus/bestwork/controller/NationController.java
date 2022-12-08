package com.nineplus.bestwork.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.entity.NationEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.INationService;
import com.nineplus.bestwork.utils.CommonConstants;

@RestController
@RequestMapping(value = "api/v1/nations")
public class NationController extends BaseController {

	@Autowired
	private INationService nationService;

	@GetMapping("/list")
	public ResponseEntity<? extends Object> getAllAsianNations() throws BestWorkBussinessException {
		List<NationEntity> nationList = new ArrayList<>();
		try {
			nationList = this.nationService.findAll();
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SNA0002, nationList, null);
	}
}
