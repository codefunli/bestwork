package com.nineplus.bestwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.ConstructionReqDto;
import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IConstructionService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * 
 * @author DiepTT
 *
 */
@RestController
@RequestMapping(value = "/api/v1/constructions")
public class ConstructionController extends BaseController {

	@Autowired
	private IConstructionService constructionService;

	/**
	 * Function: get constructions with condition (current user, keyword, pageable)
	 * 
	 * @param pageCondition
	 * @return (ResponseEntity<apiResponseDto>) all constructions of projects that
	 *         current user being involved (creating or being assigned)
	 */
	@PostMapping("/list")
	public ResponseEntity<? extends Object> getAllConstructions(@RequestBody PageSearchDto pageCondition) {
		PageResDto<ConstructionResDto> pageConstructions = null;
		try {
			pageConstructions = constructionService.getPageConstructions(pageCondition);
		} catch (BestWorkBussinessException ex) {
			return failed(CommonConstants.MessageCode.ECS0001, ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCS0001, pageConstructions, null);
	}

	/**
	 * Function: create construction with condition (current user is contractor and
	 * one of the air way bills for the construction is already customs cleared and
	 * all the air way bills for the construction must exist in the current project)
	 * 
	 * @param constructionReqDto
	 * @return (ResponseEntity<apiResponseDto>) message that construction is created
	 *         successfully or not
	 */
	@PostMapping("/create")
	public ResponseEntity<? extends Object> createConstruction(@RequestBody ConstructionReqDto constructionReqDto) {
		try {
			constructionService.createConstruction(constructionReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCS0002, null, null);
	}

	@GetMapping("/detail/{constructionId}")
	public ResponseEntity<? extends Object> getConstructionById(@PathVariable long constructionId)
			throws BestWorkBussinessException {
		ConstructionResDto constructionResDto = null;

		constructionResDto = constructionService.findConstructionById(constructionId);
		if (constructionResDto == null) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		} else {
			return success(CommonConstants.MessageCode.SCS0003, constructionResDto, null);
		}
	}

}
