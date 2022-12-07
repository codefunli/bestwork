package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.EvidenceAfterReqDto;
import com.nineplus.bestwork.dto.EvidenceAfterResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.entity.EvidenceAfterPost;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author TuanNA
 *
 */
public interface IEvidenAfterPostService {

	void updateEvidenceAfter(EvidenceAfterReqDto evidenceAfterReqDto, List<MultipartFile> mFiles)
			throws BestWorkBussinessException;

	List<EvidenceAfterResDto> getAllEvidenceAfter(long awbId) throws BestWorkBussinessException;

	EvidenceAfterPost pushComment(Long evidenceAfterPostId, PostCommentReqDto postCommentRequestDto)
			throws BestWorkBussinessException;

}