package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.ConstructionListIdDto;
import com.nineplus.bestwork.dto.ConstructionReqDto;
import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.entity.ConstructionEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;

/**
 * 
 * @author DiepTT
 *
 */
public interface IConstructionService {

	PageResDto<ConstructionResDto> getPageConstructions(PageSearchDto pageCondition) throws BestWorkBussinessException;

	void createConstruction(ConstructionReqDto constructionReqDto, List<MultipartFile> drawings)
			throws BestWorkBussinessException;

	ConstructionResDto findCstrtResById(long constructionId) throws BestWorkBussinessException;

	void updateConstruction(long constructionId, ConstructionReqDto constructionReqDto, List<MultipartFile> drawings)
			throws BestWorkBussinessException;

	void deleteConstruction(ConstructionListIdDto constructionListId) throws BestWorkBussinessException;

	ConstructionEntity findCstrtById(long constructionId);

	Boolean chkCurUserCanCreateCstrt(UserAuthDetected userAuthDetected, String prjCode)
			throws BestWorkBussinessException;

	ConstructionEntity findCstrtByPrgId(Long progressId);

}
