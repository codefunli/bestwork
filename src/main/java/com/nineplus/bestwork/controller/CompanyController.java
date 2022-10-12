package com.nineplus.bestwork.controller;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.RCompanyReqDTO;
import com.nineplus.bestwork.dto.RCompanyResDTO;
import com.nineplus.bestwork.dto.RCompanyUserReqDTO;
import com.nineplus.bestwork.entity.TCompany;
import com.nineplus.bestwork.entity.TProject;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.CompanyService;
import com.nineplus.bestwork.utils.CommonConstants;

@RequestMapping(value = "/api/v1/companies")
@RestController
public class CompanyController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	CompanyService companyService;

	/**
	 * Create a company admin
	 * 
	 * @param rCompanyUserReqDTO
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@PostMapping("/create")
	public ResponseEntity<? extends Object> register(@RequestBody RCompanyUserReqDTO rCompanyUserReqDTO)
			throws BestWorkBussinessException {
		try {
			companyService.registCompany(rCompanyUserReqDTO);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0001, null, null);
	}

	/**
	 * Update a company information
	 * 
	 * @param rCompanyReqDTO
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@PutMapping("/update")
	public ResponseEntity<? extends Object> update(@RequestBody RCompanyReqDTO rCompanyReqDTO)
			throws BestWorkBussinessException {
		RCompanyResDTO rCompanyResDTO = null;
		try {
			rCompanyResDTO = companyService.updateCompany(rCompanyReqDTO);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0002, rCompanyResDTO, null);
	}

	/**
	 * Delete company and user of it
	 * 
	 * @param tCompanyId
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@DeleteMapping("/delete/{tCompanyId}")
	public ResponseEntity<? extends Object> delete(@PathVariable long tCompanyId) throws BestWorkBussinessException {
		try {
			companyService.deleteCompany(tCompanyId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0001, null, null);
	}

	@GetMapping("/{companyId}")
	public ResponseEntity<? extends Object> getCompanyAndUser(@PathVariable long companyId) {
		Optional<TCompany> company = companyService.getDetailCompany(companyId);
		if (company.isPresent()) {
			return success(CommonConstants.MessageCode.CPN0005, company, null);
		} else {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}

	}

	/**
	 * Get list company
	 * @return list company
	 */
	@GetMapping("/list")
	public ResponseEntity<? extends Object> getAllProject() {
		Collection<TCompany> companys = null;
		try {
			companys = companyService.getAllCompany();
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0006, companys, null);
	}
}
