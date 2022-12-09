package com.nineplus.bestwork.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.CompanyBriefResDto;
import com.nineplus.bestwork.dto.ConstructionReqDto;
import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.ConstructionStatusResDto;
import com.nineplus.bestwork.dto.IdsToDelReqDto;
import com.nineplus.bestwork.dto.NationResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchConstrctDto;
import com.nineplus.bestwork.dto.ProjectResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IConstructionService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.ConstructionStatus;

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
	 * @return (ResponseEntity<apiResponseDto>) message and list of all
	 *         constructions of projects that current user being involved (creating
	 *         or being assigned) if success
	 */
	@PostMapping("/list")
	public ResponseEntity<? extends Object> getAllConstructions(@RequestBody PageSearchConstrctDto pageCondition) {
		PageResDto<ConstructionResDto> pageConstructions = null;
		try {
			pageConstructions = constructionService.getPageConstructions(pageCondition);
		} catch (BestWorkBussinessException ex) {
			return failed(CommonConstants.MessageCode.ECS0001, ex.getParam());
		}
		if (ObjectUtils.isEmpty(pageConstructions.getContent())) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
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
	public ResponseEntity<? extends Object> createConstruction(@RequestPart ConstructionReqDto constructionReqDto,
			@RequestPart List<MultipartFile> drawings) {
		try {
			constructionService.createConstruction(constructionReqDto, drawings);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCS0002, null, null);
	}

	/**
	 * Function: get construction by construction id
	 * 
	 * @param constructionId
	 * @return (ResponseEntity<apiResponseDto>) message that getting construction is
	 *         successful or not and construction details if successful
	 */
	@GetMapping("/detail/{constructionId}")
	public ResponseEntity<? extends Object> getConstructionById(@PathVariable long constructionId) {
		ConstructionResDto constructionResDto = null;
		try {
			constructionResDto = constructionService.findCstrtResById(constructionId);
		} catch (BestWorkBussinessException e) {
			return failed(e.getMsgCode(), e.getParam());
		}

		if (constructionResDto == null) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		} else {
			return success(CommonConstants.MessageCode.SCS0003, constructionResDto, null);
		}
	}

	/**
	 * Function: update construction by construction id
	 * 
	 * @param constructionId
	 * @param constructionReqDto
	 * @return (ResponseEntity<apiResponseDto>) message that updating construction
	 *         is successful or not
	 * @throws IOException
	 */
	@PatchMapping("/update/{constructionId}")
	public ResponseEntity<? extends Object> updateConstruction(@PathVariable long constructionId,
			@RequestPart ConstructionReqDto constructionReqDto, @RequestPart List<MultipartFile> drawings)
			throws IOException {
		try {
			constructionService.updateConstruction(constructionId, constructionReqDto, drawings);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCS0004, null, null);
	}

	/**
	 * Function: delete constructions by list of construction ids
	 * 
	 * @param ConstructionListIdDto
	 * @return (ResponseEntity<apiResponseDto>) message that deleting construction
	 *         is successful or not
	 */
	@PostMapping("/delete")
	public ResponseEntity<? extends Object> deleteConstruction(@RequestBody IdsToDelReqDto idsToDelReqDto) {
		try {
			constructionService.deleteConstruction(idsToDelReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCS0005, null, null);
	}

	/**
	 * Function: get list of construction status
	 * 
	 * @return (ResponseEntity<apiResponseDto>) message and list of construction
	 *         status
	 * @throws BestWorkBussinessException
	 */
	@GetMapping("/status")
	public ResponseEntity<? extends Object> getConstructionStatus() throws BestWorkBussinessException {
		List<ConstructionStatusResDto> constructionStatus = new ArrayList<>();
		for (ConstructionStatus status : ConstructionStatus.values()) {
			ConstructionStatusResDto dto = new ConstructionStatusResDto();
			dto.setId(status.ordinal());
			dto.setStatus(status.getValue());
			constructionStatus.add(dto);
		}
		return success(CommonConstants.MessageCode.SCS0008, constructionStatus, null);
	}

	@GetMapping("/companies")
	public ResponseEntity<? extends Object> getCompanyCrtPrj() throws BestWorkBussinessException {
		List<CompanyBriefResDto> companyResDtos = new ArrayList<>();
		try {
			companyResDtos = this.constructionService.getCompanyCrtPrj();
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.CPN0006, companyResDtos, null);
	}

	@GetMapping("/projects")
	public ResponseEntity<? extends Object> getPrj4CurUser() throws BestWorkBussinessException {
		List<ProjectResDto> prjResDtos = new ArrayList<>();
		try {
			prjResDtos = this.constructionService.getPrjForCurUser();
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0006, prjResDtos, null);
	}

	@GetMapping("/nations")
	public ResponseEntity<? extends Object> getNationsByCurCstrt() throws BestWorkBussinessException {
		List<NationResDto> nationResDtos = new ArrayList<>();
		try {
			nationResDtos = this.constructionService.getNationsByCurCstrt();
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SNA0002, nationResDtos, null);
	}

	@PatchMapping("/close/{constructionId}")
	public ResponseEntity<? extends Object> closeCstrt(@PathVariable long constructionId)
			throws BestWorkBussinessException {
		try {
			this.constructionService.closeCstrt(constructionId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SCS0009, null, null);
	}

	@GetMapping("/test-api")
	public ResponseEntity<? extends Object> testApi() {
		return success(CommonConstants.MessageCode.S1X0002, null, null);
	}
}
