package com.nineplus.bestwork.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.CompanyListIdDto;
import com.nineplus.bestwork.dto.CompanyReqDto;
import com.nineplus.bestwork.dto.CompanyResDto;
import com.nineplus.bestwork.dto.CompanyUserReqDto;
import com.nineplus.bestwork.dto.CompanyUserResDto;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.UserReqDto;
import com.nineplus.bestwork.dto.UserResDto;
import com.nineplus.bestwork.entity.TCompany;
import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.TCompanyRepository;
import com.nineplus.bestwork.repository.TRoleRepository;
import com.nineplus.bestwork.repository.TUserRepository;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.DateUtils;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;
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
	TUserRepository tUserRepos;

	@Autowired
	DateUtils dateUtils;

	@Autowired
	PageUtils responseUtils;

	@Autowired
	ConvertResponseUtils convertResponseUtils;

	@Transactional(rollbackFor = { Exception.class })
	public void registCompany(CompanyUserReqDto companyReqDto) throws BestWorkBussinessException {

		// Check role of user
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		String createUser = userAuthRoleReq.getUsername();
		if (!userAuthRoleReq.getIsSysAdmin()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		// validate company information
		this.validateCpmnyInfor(companyReqDto.getCompany(), false);

		// validate user information
		this.validateUserInfor(companyReqDto.getUser());

		try {
			// Register company information in DB
			companyReqDto.getCompany().setCreateBy(createUser);
			;
			TCompany newCompanySaved = regist(companyReqDto);
			TRole role = roleRepository.findRole(CommonConstants.RoleName.ORG_ADMIN);

			// Register user for this company
			userService.registNewUser(companyReqDto.getUser(), newCompanySaved, role);
		} catch (BestWorkBussinessException ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001,
					new Object[] { CommonConstants.Character.COMPANY, companyReqDto.getCompany().getCompanyName() }),
					ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001,
					new Object[] { CommonConstants.Character.COMPANY, companyReqDto.getCompany().getCompanyName() });
		}
	}

	/**
	 */
	public void validateCpmnyInfor(CompanyReqDto companyReqDto, boolean isEdit) throws BestWorkBussinessException {
		// Validation register information
		String companyName = companyReqDto.getCompanyName();

		// Company name can not be empty
		if (ObjectUtils.isEmpty(companyName)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.CMPNY_NAME });
		}

		// Check exists company name in database
		if (!isEdit) {
			TCompany company = tCompanyRepository.findbyCompanyName(companyName);
			if (!ObjectUtils.isEmpty(company)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.CPN0005, new Object[] { company });
			}
		}

	}

	public void validateUserInfor(UserReqDto userReq) throws BestWorkBussinessException {
		String userEmail = userReq.getEmail();
		String userName = userReq.getUserName();
		String password = userReq.getPassword();
		// User email can not be empty
		if (ObjectUtils.isEmpty(userEmail)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.USER_MAIL });
		}

		// User name can not be empty
		if (ObjectUtils.isEmpty(userName)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.USER_NAME });
		}

		// Password can not be empty empty
		if (ObjectUtils.isEmpty(password) || password.length() < 6) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.EMP0001,
					new Object[] { CommonConstants.Character.PASSWORD });
		}

		// Check exists user email in DB
		TUser user = tUserRepos.findByEmail(userEmail);
		if (!ObjectUtils.isEmpty(user)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.US0002, new Object[] { user });
		}
	}

	public TCompany regist(CompanyUserReqDto companyReqDto) throws BestWorkBussinessException {
		TCompany company = null;
		try {
			company = new TCompany();
			company.setCompanyName(companyReqDto.getCompany().getCompanyName());
			company.setEmail(companyReqDto.getCompany().getEmail());
			company.setTelNo(companyReqDto.getCompany().getTelNo());
			company.setTaxNo(companyReqDto.getCompany().getTaxNo());
			company.setCity(companyReqDto.getCompany().getCity());
			company.setDistrict(companyReqDto.getCompany().getDistrict());
			company.setWard(companyReqDto.getCompany().getWard());
			company.setStreet(companyReqDto.getCompany().getStreet());
			String startDt = dateUtils.convertToUTC(companyReqDto.getCompany().getStartDate());
			String expiredDt = dateUtils.convertToUTC(companyReqDto.getCompany().getExpiredDate());
			company.setStartDate(startDt);
			company.setExpiredDate(expiredDt);
			company.setCreateDt(LocalDateTime.now());
			company.setCreateBy(companyReqDto.getCompany().getCreateBy());

			tCompanyRepository.save(company);

		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0001, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0001, null);
		}

		return company;
	}

	@Transactional(rollbackFor = { Exception.class })
	public CompanyResDto updateCompany(long companyId, CompanyReqDto companyReqDto) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		// Only system admin can do this
		if (!userAuthRoleReq.getIsSysAdmin()) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}

		// validate company information
		this.validateCpmnyInfor(companyReqDto, true);

		TCompany currentCompany = null;

		try {
			currentCompany = tCompanyRepository.findByCompanyId(companyId);

			if (ObjectUtils.isEmpty(currentCompany)) {
				logger.error(messageUtils.getMessage(CommonConstants.MessageCode.CPN0003, null));
				throw new BestWorkBussinessException(CommonConstants.MessageCode.CPN0003, null);
			}
		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0003,
					new Object[] { companyReqDto.getCompanyName() }), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003,
					new Object[] { companyReqDto.getCompanyName() });
		}

		try {
			currentCompany.setCompanyName(companyReqDto.getCompanyName());
			currentCompany.setEmail(companyReqDto.getEmail());
			currentCompany.setTelNo(companyReqDto.getTelNo());
			currentCompany.setTaxNo(companyReqDto.getTaxNo());
			currentCompany.setCity(companyReqDto.getCity());
			currentCompany.setDistrict(companyReqDto.getDistrict());
			currentCompany.setWard(companyReqDto.getWard());
			currentCompany.setStreet(companyReqDto.getStreet());
			currentCompany.setStartDate(companyReqDto.getStartDate());
			currentCompany.setExpiredDate(companyReqDto.getExpiredDate());
			tCompanyRepository.save(currentCompany);

			CompanyResDto resDTO = modelMapper.map(currentCompany, CompanyResDto.class);
			return resDTO;

		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0004,
					new Object[] { CommonConstants.Character.COMPANY, companyReqDto.getCompanyName() }), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0004,
					new Object[] { CommonConstants.Character.COMPANY, companyReqDto.getCompanyName() });
		}
	}

	/**
	 * 
	 * @param tCompanyId
	 * @return
	 * @throws BestWorkBussinessException
	 */
	@Transactional(rollbackFor = { Exception.class })
	public Long[] deleteCompany(CompanyListIdDto listId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		// Only system admin can do this
		if (!userAuthRoleReq.getIsSysAdmin()) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		try {
			// Delete company
			tCompanyRepository.deleteCompaniesWithIds(Arrays.asList(listId.getLstCompanyId()));

			// delete user relate company
			List<TUser> allTusers = tUserRepos.findAllUserByCompanyIdList(Arrays.asList(listId.getLstCompanyId()));
			tUserRepos.deleteAllInBatch(allTusers);

		} catch (Exception ex) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0002, null), ex);
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0002, null);
		}
		return listId.getLstCompanyId();
	}

	/**
	 * 
	 * @param companyId Company ID
	 * @return Company information
	 */
	public Optional<TCompany> getDetailCompany(long companyId) {
		Optional<TCompany> company = tCompanyRepository.findById(companyId);
		return company;
	}

	/**
	 * 
	 * @param companyId company ID
	 * @return Company and User information
	 * @throws BestWorkBussinessException
	 */
	public CompanyUserResDto getCompanyAndUser(long companyId) throws BestWorkBussinessException {
		CompanyUserResDto userCompanyRes = new CompanyUserResDto();
		TCompany company = tCompanyRepository.findByCompanyId(companyId);
		TUser user = userService.getUserByCompanyId(companyId);
		if (company != null && user != null) {
			CompanyResDto resCompany = modelMapper.map(company, CompanyResDto.class);
			UserResDto resUser = modelMapper.map(user, UserResDto.class);
			userCompanyRes.setCompany(resCompany);
			userCompanyRes.setUser(resUser);
		}
		return userCompanyRes;
	}

	public List<TCompany> getAllCompany() throws BestWorkBussinessException {
		return tCompanyRepository.findAll();
	}

	/**
	 * 
	 * @param pageCondition condition page
	 * @return page of company follow condition
	 * @throws BestWorkBussinessException
	 */
	public PageResponseDto<CompanyResDto> getCompanyPage(PageSearchDto pageCondition)
			throws BestWorkBussinessException {
		Page<TCompany> pageTCompany;
		try {
			int pageNumber = NumberUtils.toInt(pageCondition.getPage());

			String mappedColumn = convertResponseUtils.convertResponseCompany(pageCondition.getSortBy());
			Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageCondition.getSize()),
					Sort.by(pageCondition.getSortDirection(), mappedColumn));
			pageTCompany = tCompanyRepository.getPageCompany(pageable);
			return responseUtils.convertPageEntityToDTO(pageTCompany, CompanyResDto.class);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	/**
	 * Search company with keyword
	 * 
	 * @param keyword
	 * @param pageCondition
	 * @return
	 * @throws BestWorkBussinessException
	 */
	public PageResponseDto<CompanyResDto> searchCompanyPage(String keyword, int status, PageSearchDto pageCondition)
			throws BestWorkBussinessException {
		Page<TCompany> pageTCompany = null;
		try {
			int pageNumber = NumberUtils.toInt(pageCondition.getPage());

			String mappedColumn = convertResponseUtils.convertResponseCompany(pageCondition.getSortBy());
			Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageCondition.getSize()),
					Sort.by(pageCondition.getSortDirection(), mappedColumn));
			if (!keyword.isBlank() && status != 2) {
				pageTCompany = tCompanyRepository.searchCompanyPage(convertWildCard(keyword), status, pageable);
			} else if (keyword.isBlank()) {
				pageTCompany = tCompanyRepository.searchCompanyPageWithOutKeyWord(status, pageable);
			} else if (status == 2 && !keyword.isBlank()) {
				pageTCompany = tCompanyRepository.searchCompanyPageWithOutStatus(convertWildCard(keyword), pageable);
			}
			return responseUtils.convertPageEntityToDTO(pageTCompany, CompanyResDto.class);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	private String convertWildCard(String text) {
		return "*" + text + "*";
	}

}
