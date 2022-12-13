package com.nineplus.bestwork.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.EvidenceAfterReqDto;
import com.nineplus.bestwork.dto.EvidenceAfterResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.entity.EvidenceAfterPost;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IEvidenAfterPostService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * 
 * @author TuanNA
 *
 */
@RestController
@RequestMapping("/api/v1/evidence-after-post")
public class EvidenceAfterPostController extends BaseController {

	@Autowired
	IEvidenAfterPostService iEvidenAfterPostService;

	@PatchMapping("/update")
	public ResponseEntity<? extends Object> update(@RequestPart EvidenceAfterReqDto evidenceAfter,
			@RequestPart List<MultipartFile> mFiles) throws BestWorkBussinessException {
		try {
			iEvidenAfterPostService.updateEvidenceAfter(evidenceAfter, mFiles);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sEA0001, null, null);
	}

	@GetMapping("/list/by/{awbId}")
	public ResponseEntity<? extends Object> getAllEvidenceAfter(@PathVariable long awbId)
			throws BestWorkBussinessException {
		List<EvidenceAfterResDto> listEvidenceAfter = null;
		try {
			listEvidenceAfter = iEvidenAfterPostService.getAllEvidenceAfter(awbId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(listEvidenceAfter)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sEA0002, listEvidenceAfter, null);
	}

	@PatchMapping("/{evidenceAfterPostId}/comment")
	public ResponseEntity<? extends Object> addComment(@PathVariable Long evidenceAfterPostId,
			@RequestBody PostCommentReqDto postCommentRequestDto) throws BestWorkBussinessException {
		EvidenceAfterPost evidenceAfter =  null;
		try {
			evidenceAfter = iEvidenAfterPostService.pushComment(evidenceAfterPostId, postCommentRequestDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sEA0003, evidenceAfter, null);
	}

}
