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

import com.nineplus.bestwork.dto.EvidenceBeforeReqDto;
import com.nineplus.bestwork.dto.EvidenceBeforeResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.entity.EvidenceBeforePost;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.IEvidenBeforePostService;
import com.nineplus.bestwork.utils.CommonConstants;

/**
 * 
 * @author TuanNA
 *
 */
@RestController
@RequestMapping("/api/v1/evidence-before-post")
public class EvidenceBeforePostController extends BaseController {

	@Autowired
	IEvidenBeforePostService iEvidenBeforePostService;

	@PatchMapping("/update")
	public ResponseEntity<? extends Object> update(@RequestPart EvidenceBeforeReqDto evidenceBefore,
			@RequestPart List<MultipartFile> mFiles) throws BestWorkBussinessException {
		try {
			iEvidenBeforePostService.updateEvidenceBefore(evidenceBefore, mFiles);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sEB0001, null, null);
	}

	@GetMapping("/list/by/{airWayBillId}")
	public ResponseEntity<? extends Object> getAllEvidenceBefore(@PathVariable String airWayBillId)
			throws BestWorkBussinessException {
		List<EvidenceBeforeResDto> listEvidenceBefore = null;
		try {
			listEvidenceBefore = iEvidenBeforePostService.getAllEvidenceBefore(airWayBillId);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		if (ObjectUtils.isEmpty(listEvidenceBefore)) {
			return success(CommonConstants.MessageCode.E1X0003, null, null);
		}
		return success(CommonConstants.MessageCode.sEB0002, listEvidenceBefore, null);
	}

	@PatchMapping("/{evidenceBeforePostId}/comment")
	public ResponseEntity<? extends Object> addComment(@PathVariable Long evidenceBeforePostId,
			@RequestBody PostCommentReqDto postCommentRequestDto) throws BestWorkBussinessException {
		EvidenceBeforePost evidenceBefore = null;
		try {
			evidenceBefore = iEvidenBeforePostService.pushComment(evidenceBeforePostId, postCommentRequestDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.sEB0003, evidenceBefore, null);
	}

}
