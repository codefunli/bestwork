package com.nineplus.bestwork.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.ProgressAndConstructionResDto;
import com.nineplus.bestwork.dto.ProgressListReqDto;
import com.nineplus.bestwork.dto.ProgressReqDto;
import com.nineplus.bestwork.dto.ProgressResDto;
import com.nineplus.bestwork.dto.ProgressStatusResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IProgressService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.ProgressStatus;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController extends BaseController {

	@Autowired
	private IProgressService progressService;

	@PostMapping("/create")
	public ResponseEntity<? extends Object> createProgress(@RequestBody ProgressReqDto progressReqDto)
			throws BestWorkBussinessException {
		try {
			progressService.registProgress(progressReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu00001, null, null);
	}

	@PostMapping("/update/{progressId}")
	public ResponseEntity<? extends Object> updateProgress(@RequestBody ProgressReqDto progressReqDto,
			@PathVariable long progressId) throws BestWorkBussinessException {
		try {
			progressService.updateProgress(progressReqDto, progressId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu00002, null, null);
	}

//	@GetMapping("by/project/{projectId}")
//	public ResponseEntity<? extends Object> getAllProgressByCompanyId(@PathVariable String projectId)
//			throws BestWorkBussinessException {
//		ProgressAndProjectResDto progressAndProjectDto = null;
//		try {
//			progressAndProjectDto = progressService.getProjectAndProgress(projectId);
//		} catch (BestWorkBussinessException ex) {
//			return failed(ex.getMsgCode(), ex.getParam());
//		}
//		return success(CommonConstants.MessageCode.sPu00003, progressAndProjectDto, null);
//	}

	@GetMapping("/by/construction/{constructionId}")
	public ResponseEntity<? extends Object> getAllProgressByConstruction(@PathVariable String constructionId) {
		ProgressAndConstructionResDto progressAndConstructionDto = null;
		try {
			progressAndConstructionDto = progressService.getProgressByConstruction(constructionId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu00003, progressAndConstructionDto, null);
	}

	@PostMapping("/delete")
	public ResponseEntity<? extends Object> deleteProgress(@RequestBody ProgressListReqDto listId)
			throws BestWorkBussinessException {
		try {
			progressService.deleteProgressList(Arrays.asList(listId.getLstProgressId()));
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu0004, null, null);
	}

	@GetMapping("/{progressId}")
	public ResponseEntity<? extends Object> getProgressId(@PathVariable Long progressId)
			throws BestWorkBussinessException {
		ProgressResDto progress = null;
		try {
			progress = progressService.getProgressById(progressId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu0005, progress, null);
	}

	@GetMapping("/status")
	public ResponseEntity<? extends Object> getProgressStatus() throws BestWorkBussinessException {
		List<ProgressStatusResDto> progressStatusLst = new ArrayList<>();
		for (ProgressStatus status : ProgressStatus.values()) {
			ProgressStatusResDto dto = new ProgressStatusResDto();
			dto.setId(status.ordinal());
			dto.setStatus(status.getValue());
			progressStatusLst.add(dto);
		}
		return success(CommonConstants.MessageCode.sPu0006, progressStatusLst, null);
	}
}
