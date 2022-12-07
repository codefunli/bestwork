package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.EvidenceBeforeReqDto;
import com.nineplus.bestwork.dto.EvidenceBeforeResDto;
import com.nineplus.bestwork.dto.PostCommentReqDto;
import com.nineplus.bestwork.entity.EvidenceBeforePost;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author TuanNA
 *
 */
public interface IEvidenBeforePostService {

	void updateEvidenceBefore(EvidenceBeforeReqDto evidenceBeforeReqDto, List<MultipartFile> mFiles)
			throws BestWorkBussinessException;

	List<EvidenceBeforeResDto> getAllEvidenceBefore(long awbId) throws BestWorkBussinessException;

	EvidenceBeforePost pushComment(Long evidenceBeforePostId, PostCommentReqDto postCommentRequestDto)
			throws BestWorkBussinessException;

}
