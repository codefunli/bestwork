package com.nineplus.bestwork.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.IdsToDelReqDto;
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
	public ResponseEntity<? extends Object> createProgress(@RequestPart ProgressReqDto progressReqDto,
			@RequestPart List<MultipartFile> fileBefore, @RequestPart List<MultipartFile> fileAfter) throws BestWorkBussinessException {
		try {
			progressService.registProgress(progressReqDto, fileBefore,fileAfter);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu00001, null, null);
	}

	@PostMapping("/update/{progressId}")
	public ResponseEntity<? extends Object> updateProgress(@RequestPart ProgressReqDto progressReqDto,
			@RequestPart List<MultipartFile> fileBefore, @RequestPart List<MultipartFile> fileAfter, @PathVariable long progressId) throws BestWorkBussinessException {
		try {
			progressService.updateProgress(progressReqDto, fileBefore, fileAfter, progressId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu00002, null, null);
	}

	@GetMapping("/by/construction/{constructionId}")
	public ResponseEntity<? extends Object> getAllProgressByConstruction(@PathVariable String constructionId) {
		List<ProgressResDto> progressDtoList = null;
		try {
			progressDtoList = progressService.getProgressByConstruction(constructionId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(progressDtoList)) {
			return success(CommonConstants.MessageCode.E1X0003, progressDtoList, null);
		}
		return success(CommonConstants.MessageCode.sPu00003, progressDtoList, null);
	}

	@PostMapping("/delete")
	public ResponseEntity<? extends Object> deleteProgress(@RequestBody IdsToDelReqDto listId)
			throws BestWorkBussinessException {
		try {
			progressService.deleteProgressList(Arrays.asList(listId.getListId()));
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sPu0004, null, null);
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
