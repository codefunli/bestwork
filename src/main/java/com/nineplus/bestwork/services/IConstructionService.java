package com.nineplus.bestwork.services;

import com.nineplus.bestwork.dto.ConstructionReqDto;
import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
/**
 * 
 * @author DiepTT
 *
 */
public interface IConstructionService {

	PageResDto<ConstructionResDto> getPageConstructions(PageSearchDto pageCondition) throws BestWorkBussinessException;

	void createConstruction(ConstructionReqDto constructionReqDto) throws BestWorkBussinessException;

	ConstructionResDto findConstructionById(long constructionId) throws BestWorkBussinessException;

	void updateConstruction(long constructionId, ConstructionReqDto constructionReqDto) throws BestWorkBussinessException;

	void deleteConstruction(long constructionId) throws BestWorkBussinessException;
}
