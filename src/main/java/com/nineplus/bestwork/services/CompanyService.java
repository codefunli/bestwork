package com.nineplus.bestwork.services;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.RCompanyReqDTO;
import com.nineplus.bestwork.dto.RCompanyResDTO;
import com.nineplus.bestwork.dto.RCompanyUserReqDTO;
import com.nineplus.bestwork.entity.TCompany;
import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.TRoleRepository;
import com.nineplus.bestwork.repository.TCompanyRepository;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class CompanyService {

	private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	MessageUtils messageUtils;

	@Autowired
	TRoleRepository roleRepository;

	@Autowired
	TCompanyRepository tCompanyRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserService userService;

	@Autowired
	DateUtils dateUtils;

	@Transactional(rollbackFor = { Exception.class })
	public void registCompany(RCompanyUserReqDTO companyReqDto) throws BestWorkBussinessException {

		// Check role of user
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		if (!userAuthRoleReq.getIsSysAdmin()) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		// validation
		this.validateInformation(companyReqDto);

		try {
			// Register company information in DB
			TCompany newCompanySaved = regist(companyReqDto);
			TRole role = roleRepository.findRole(CommonConstants.RoleName.ORG_ADMIN);

			// Register user for this company
			userService.registNewUser(companyReqDto.getUser(), newCompanySaved, role);
		} catch (BestWorkBussinessException ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001,
					new Object[] { CommonConstants.Character.COMPANY, companyReqDto.getCompany().getCompanyName() }),
					ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
		}
	}

	public void validateInformation(RCompanyUserReqDTO companyReqDto) throws BestWorkBussinessException {
		// Validation register information

	}

	public TCompany regist(RCompanyUserReqDTO companyReqDto) throws BestWorkBussinessException {
		TCompany company = null;
		try {
			company = new TCompany();
			company.setCompanyId(companyReqDto.getCompany().getCompanyId());
			company.setCompanyName(companyReqDto.getCompany().getCompanyName());
			company.setEmail(companyReqDto.getCompany().getEmail());
			company.setTelNo(companyReqDto.getCompany().getTelNo());
			company.setTaxNo(companyReqDto.getCompany().getTaxNo());
			company.setCity(companyReqDto.getCompany().getCity());
			company.setDistrict(companyReqDto.getCompany().getDistrict());
			company.setWard(companyReqDto.getCompany().getWard());
			company.setStreet(companyReqDto.getCompany().getStreet());
			company.setDetailAddress(companyReqDto.getCompany().getDetailAddress());
			//String startDt = dateUtils.convertToUTC(companyReqDto.getCompany().getStartDate());
			//String expiredDt = dateUtils.convertToUTC(companyReqDto.getCompany().getExpiredDate());
			company.setStartDate(companyReqDto.getCompany().getStartDate());
			company.setExpiredDate(companyReqDto.getCompany().getExpiredDate());

			tCompanyRepository.save(company);

		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
		}

		return company;
	}

	@Transactional(rollbackFor = { Exception.class })
	public RCompanyResDTO updateCompany(RCompanyReqDTO rcompanyReqDto) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		// Only system admin can do this
		if (!userAuthRoleReq.getIsSysAdmin()) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		TCompany currentCompany = null;

		try {
			currentCompany = tCompanyRepository.findById(Long.valueOf(rcompanyReqDto.getId())).orElse(null);
			if (ObjectUtils.isEmpty(currentCompany)) {
				logger.error(messageUtils.getMessage(CommonConstants.MessageCode.CPN0003, null));
				throw new BestWorkBussinessException(CommonConstants.MessageCode.CPN0003, null);
			}
		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003,
					new Object[] { rcompanyReqDto.getCompanyName() }), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003,
					new Object[] { rcompanyReqDto.getCompanyName() });
		}

		try {
			currentCompany.setCompanyId(rcompanyReqDto.getCompanyId());
			currentCompany.setCompanyName(rcompanyReqDto.getCompanyName());
			currentCompany.setEmail(rcompanyReqDto.getEmail());
			currentCompany.setTelNo(rcompanyReqDto.getTelNo());
			currentCompany.setTaxNo(rcompanyReqDto.getTaxNo());
			currentCompany.setCity(rcompanyReqDto.getCity());
			currentCompany.setDistrict(rcompanyReqDto.getDistrict());
			currentCompany.setWard(rcompanyReqDto.getWard());
			currentCompany.setStreet(rcompanyReqDto.getStreet());
			currentCompany.setDetailAddress(rcompanyReqDto.getDetailAddress());
			currentCompany.setStartDate(rcompanyReqDto.getStartDate());
			currentCompany.setExpiredDate(rcompanyReqDto.getExpiredDate());
			tCompanyRepository.save(currentCompany);

			RCompanyResDTO resDTO = modelMapper.map(currentCompany, RCompanyResDTO.class);
			return resDTO;

		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0004,
					new Object[] { CommonConstants.Character.COMPANY, rcompanyReqDto.getCompanyName() }), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0004,
					new Object[] { CommonConstants.Character.COMPANY, rcompanyReqDto.getCompanyName() });
		}
	}

	public void delete(String tCompanyId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		// Only system admin can do this
		if (!userAuthRoleReq.getIsSysAdmin()) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
	}
}
