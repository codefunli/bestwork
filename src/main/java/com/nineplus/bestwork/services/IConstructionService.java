package com.nineplus.bestwork.services;

import java.io.IOException;
import java.util.List;

import com.nineplus.bestwork.dto.*;
import org.springframework.web.multipart.MultipartFile;

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
			throws BestWorkBussinessException, IOException;

	void deleteConstruction(IdsToDelReqDto idsToDelReqDto) throws BestWorkBussinessException;

	ConstructionEntity findCstrtById(long constructionId);

	Boolean chkCurUserCanCreateCstrt(UserAuthDetected userAuthDetected, String prjCode)
			throws BestWorkBussinessException;

	ConstructionEntity findCstrtByPrgId(Long progressId);

	void updateStsConstruction(long id, String status) throws BestWorkBussinessException;

	Integer countConstructionUser(String username);

	List<CountLocationDto> getLocationsUser(String username);

}
