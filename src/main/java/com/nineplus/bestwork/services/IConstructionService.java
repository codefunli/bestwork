package com.nineplus.bestwork.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nineplus.bestwork.dto.CompanyBriefResDto;
import com.nineplus.bestwork.dto.ConstructionReqDto;
import com.nineplus.bestwork.dto.ConstructionResDto;
import com.nineplus.bestwork.dto.CountLocationDto;
import com.nineplus.bestwork.dto.IdsToDelReqDto;
import com.nineplus.bestwork.dto.NationResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchConstrctDto;
import com.nineplus.bestwork.dto.ProjectResDto;
import com.nineplus.bestwork.entity.ConstructionEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;

/**
 * 
 * @author DiepTT
 *
 */
public interface IConstructionService {

	PageResDto<ConstructionResDto> getPageConstructions(PageSearchConstrctDto pageCondition)
			throws BestWorkBussinessException;

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

	List<CompanyBriefResDto> getCompanyCrtPrj() throws BestWorkBussinessException;

	List<ProjectResDto> getPrjForCurUser() throws BestWorkBussinessException;

	List<NationResDto> getNationsByCurCstrt() throws BestWorkBussinessException;

	void updateStsConstruction(long id, String status) throws BestWorkBussinessException;

	void closeCstrt(long constructionId) throws BestWorkBussinessException;

	Integer countConstructionUser(String username);

	List<CountLocationDto> getLocationsUser(String username);

}
