package com.nineplus.bestwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.PostResponseDto;
import com.nineplus.bestwork.dto.ProgressReqDto;
import com.nineplus.bestwork.dto.ProgressResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IProgressService;
import com.nineplus.bestwork.utils.CommonConstants;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController extends BaseController {

	@Autowired
	private IProgressService progressService;

	@PostMapping("/create")
	public ResponseEntity<? extends Object> createPost(@RequestBody ProgressReqDto progressReqDto)
			throws BestWorkBussinessException {
		try {
			progressService.saveProgress(progressReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0004, null, null);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<? extends Object> getAllProgressByCompanyId(@PathVariable String projectId)
			throws BestWorkBussinessException {
		List<ProgressResDto> progress = null;
		try {
			progress = progressService.getProgressByProjectId(projectId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.S1X0004, progress, null);
	}

}
