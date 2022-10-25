package com.nineplus.bestwork.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.CompanyListIdDto;
import com.nineplus.bestwork.dto.CompanyReqDto;
import com.nineplus.bestwork.dto.CompanyResDto;
import com.nineplus.bestwork.dto.CompanyUserReqDto;
import com.nineplus.bestwork.dto.CompanyUserResDto;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.CompanyProjection;
import com.nineplus.bestwork.services.CompanyService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;

@RequestMapping(value = "/api/v1/companies")
@RestController
public class CompanyController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	CompanyService companyService;

	@Autowired
	UserService userService;

	private final int DEFAULT_STATUS = 2;

	/**
	 * Create a company admin
	 * 
	 * @param rCompanyUserReqDTO
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@PostMapping("/create")
	public ResponseEntity<? extends Object> register(@RequestBody CompanyUserReqDto rCompanyUserReqDTO)
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
	@PutMapping("/update/{companyId}")
	public ResponseEntity<? extends Object> update(@PathVariable long companyId,
			@RequestBody CompanyReqDto rCompanyReqDTO) throws BestWorkBussinessException {
		CompanyResDto rCompanyResDTO = null;
		try {
			rCompanyResDTO = companyService.updateCompany(companyId, rCompanyReqDTO);
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
	@PostMapping("/delete")
	public ResponseEntity<? extends Object> delete(@RequestBody CompanyListIdDto listId)
			throws BestWorkBussinessException {
		try {
			companyService.deleteCompany(listId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0004, null, null);
	}

	@GetMapping("/{companyId}")
	public ResponseEntity<? extends Object> getCompanyAndUser(@PathVariable long companyId)
			throws BestWorkBussinessException {
		CompanyUserResDto companyUserRes = companyService.getCompanyAndUser(companyId);
		if (companyUserRes.getCompany() != null || companyUserRes.getUser() != null) {
			return success(CommonConstants.MessageCode.CPN0005, companyUserRes, null);
		} else {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}

	}

	/**
	 * Get list company
	 * 
	 * @return list company
	 */
	@PostMapping("/list")
	public ResponseEntity<? extends Object> getAllCompany(@RequestBody PageSearchDto pageCondition) {
		PageResponseDto<CompanyResDto> pageCompany = null;
		try {
			if (pageCondition.getKeyword().isEmpty() && pageCondition.getStatus() == DEFAULT_STATUS) {
				pageCompany = companyService.getCompanyPage(pageCondition);
			} else {
				pageCompany = companyService.searchCompanyPage(pageCondition.getKeyword(), pageCondition.getStatus(),
						pageCondition);
			}
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0006, pageCompany, null);
	}
	
	@GetMapping("/all")
	public ResponseEntity<? extends Object> getAll() throws BestWorkBussinessException {
		List<CompanyProjection> listCompany = null;
		try {
			listCompany = companyService.getAllCompany();
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0001, listCompany, null);
	}

}
