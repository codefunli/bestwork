package com.nineplus.bestwork.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.CompanyUserReqDto;
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchUserDto;
import com.nineplus.bestwork.dto.RPageDto;
import com.nineplus.bestwork.dto.UserCompanyReqDto;
import com.nineplus.bestwork.dto.UserListIdDto;
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
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class UserService implements UserDetailsService {
	int countUserLoginFailedBlocked = 5;

	private final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	TUserRepository tUserRepo;

	public void saveUser(TUser user) {
		tUserRepo.save(user);
	}

	public TUser getUserByUsername(String userName) {
		return tUserRepo.findByUserName(userName);
	}

	public boolean isBlocked(int countLoginFailed) {
		return countLoginFailed >= countUserLoginFailedBlocked;
	}

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	ConvertResponseUtils convertResponseUtils;

	@Autowired
	PageUtils responseUtils;

	@Autowired
	TCompanyRepository companyRepository;

	@Autowired
	TRoleRepository roleRepository;

	@Autowired
	MessageUtils messageUtils;

	@Autowired
	TCompanyRepository tCompanyRepository;

	@Autowired
	MailSenderService mailSenderService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		TUser user = tUserRepo.findByUserName(userName);
		if (ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("User not found");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
		return new User(user.getUserName(), user.getPassword(), authorities);
	}

	public UserResDto convertUserToUserDto(TUser user) {
		UserResDto dto = null;
		if (user != null) {
			dto = new UserResDto();
			dto.setId(user.getId());
			dto.setUserName(user.getUserName());
			dto.setEmail(user.getEmail());
			dto.setRole(user.getRole());
			dto.setEnabled(user.getIsEnable());
			dto.setRole(user.getRole());
			dto.setEnabled(user.getIsEnable());
			dto.setTelNo(user.getTelNo());
			dto.setFirstName(user.getFirstNm());
			dto.setLastName(user.getLastNm());
		}

		return dto;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void registNewUser(CompanyUserReqDto companyReqDto, TCompany tCompany, TRole tRole) {
		TUser newTUser = new TUser();
		Set<TCompany> tCompanyUser = new HashSet<TCompany>();
		UserCompanyReqDto newUser = companyReqDto.getUser();
		tCompanyUser.add(tCompany);
		newTUser.setEmail(newUser.getEmail());
		newTUser.setUserName(newUser.getUserName());
		newTUser.setIsEnable(newUser.getEnabled());
		newTUser.setFirstNm(newUser.getFirstName());
		newTUser.setLastNm(newUser.getLastName());
		newTUser.setLoginFailedNum(0);
		newTUser.setPassword(encoder.encode(newUser.getPassword()));
		newTUser.setTelNo(newUser.getTelNo());
		newTUser.setRole(tRole);
		newTUser.setCompanys(tCompanyUser);

		tUserRepo.save(newTUser);
		String linkLogin = messageUtils.getMessage(CommonConstants.Url.URL0001, null) + "/login";
		mailSenderService.sendMailRegisterUserCompany(newUser.getEmail(), companyReqDto, linkLogin);
	}

	public TUser getUserByCompanyId(long companyId) {
		return tUserRepo.findUserByOrgId(companyId);

	}

	public long findCompanyIdByAdminUsername(UserAuthDetected userAuthRoleReq) {
		String companyAdminUserName = userAuthRoleReq.getUsername();
		long companyId = tUserRepo.findCompanyIdByAdminUsername(companyAdminUserName);
		return companyId;
	}

	private List<TUser> getUsersByKeyword(String keyword) {
		return this.tUserRepo.getUsersByKeyword(keyword);
	}

	@Transactional(rollbackFor = { Exception.class })
	public TUser createUser(UserReqDto userReqDto) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		String createUser = userAuthRoleReq.getUsername();
		TRole role = new TRole();
		if (ObjectUtils.isNotEmpty(userReqDto.getRole())) {
			Optional<TRole> roleOptional = roleRepository.findById(userReqDto.getRole());
			if (roleOptional.isPresent()) {
				role = roleOptional.get();
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eR0002, null);
			}
		}
		Set<TCompany> companies = new HashSet<>();
		if (ObjectUtils.isNotEmpty(userReqDto.getCompany())) {
			TCompany companyCurrent = companyRepository.findByCompanyId(userReqDto.getCompany());
			if (companyCurrent != null) {
				companies.add(companyCurrent);
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.CPN0003, null);
			}
		}

		TUser user = new TUser();
		user.setCompanys(companies);
		user.setUserName(userReqDto.getUserName());
		user.setPassword(encoder.encode(userReqDto.getPassword()));
		user.setFirstNm(userReqDto.getFirstName());
		user.setLastNm(userReqDto.getLastName());
		user.setEmail(userReqDto.getEmail());
		user.setTelNo(userReqDto.getTelNo());
		user.setIsEnable(userReqDto.getEnabled());
		user.setLoginFailedNum(0);
		user.setRole(role);
		user.setCreateBy(createUser);
		user.setCreateDate(LocalDateTime.now());
		user.setDeleteFlag(0);
		if (null != userReqDto.getAvatar()) {
			user.setUserAvatar(userReqDto.getAvatar().getBytes());
		}
		return this.tUserRepo.save(user);
	}

	public List<TUser> findAllUsersByCompanyId(long companyId) {
		return this.tUserRepo.findAllUsersByCompanyId(companyId);
	}

	public TUser getUserById(long userId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		TCompany company = tCompanyRepository.findById(findCompanyIdByUsername(userAuthRoleReq)).orElse(new TCompany());
		Optional<TUser> userOptional;
		if (null != company.getId()) {
			userOptional = this.tUserRepo.findUserById(userId, String.valueOf(company.getId()));
		} else {
			userOptional = this.tUserRepo.findUserById(userId, "%%");
		}
		return userOptional.orElse(null);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteUser(UserListIdDto listId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		if (!userAuthRoleReq.getIsSysAdmin()) {
			logger.error(messageUtils.getMessage(CommonConstants.MessageCode.E1X0014, null));
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		try {
			List<TUser> tUserList = tUserRepo.findAllById(Arrays.asList(listId.getUserIdList()));
			if (tUserList.isEmpty() || tUserList.size() < listId.getUserIdList().length) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, listId.getUserIdList());
			}
			tUserRepo.deleteAllByIdInBatch(Arrays.asList(listId.getUserIdList()));
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, listId.getUserIdList());
		}
	}

	public long findCompanyIdByUsername(UserAuthDetected userAuthRoleReq) {
		String companyAdminUserName = userAuthRoleReq.getUsername();
		long companyId;
		try {
			companyId = tUserRepo.findCompanyIdByAdminUsername(companyAdminUserName);
		} catch (Exception e) {
			return -1;
		}
		return companyId;
	}

	public PageResponseDto<UserResDto> getAllUsers(PageSearchUserDto pageCondition) throws BestWorkBussinessException {
		PageResponseDto<UserResDto> pageResponseDto = new PageResponseDto<>();
		Pageable pageable = convertSearch(pageCondition);
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		TCompany company = tCompanyRepository.findById(findCompanyIdByUsername(userAuthRoleReq)).orElse(new TCompany());
		Page<TUser> tUserPage;
		if (null != company.getId()) {
			tUserPage = tUserRepo.getAllUsers(pageable, String.valueOf(company.getId()), pageCondition);
		} else {
			tUserPage = tUserRepo.getAllUsers(pageable, "%%", pageCondition);
		}
		RPageDto rPageDto = createRPageDto(tUserPage);
		List<UserResDto> userResDtoList = convertTUser(tUserPage);
		pageResponseDto.setContent(userResDtoList);
		pageResponseDto.setMetaData(rPageDto);
		return pageResponseDto;
	}

	private List<UserResDto> convertTUser(Page<TUser> tUserPage) throws BestWorkBussinessException {
		List<UserResDto> userResDtoList = new ArrayList<>();
		try {
			for (TUser tUser : tUserPage.getContent()) {
				UserResDto userResDto = new UserResDto();
				userResDto.setUserName(tUser.getUserName());
				userResDto.setEmail(tUser.getEmail());
				userResDto.setFirstName(tUser.getFirstNm());
				userResDto.setLastName(tUser.getLastNm());
				userResDto.setTelNo(tUser.getTelNo());
				userResDto.setRole(tUser.getRole());
				userResDto.setId(tUser.getId());
				userResDto.setEnabled(tUser.getIsEnable());
				userResDto.setAvatar(Arrays.toString(tUser.getUserAvatar()));
				userResDtoList.add(userResDto);
			}
		} catch (Exception e) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0002, null);
		}

		return userResDtoList;
	}

	private RPageDto createRPageDto(Page<TUser> tUserPage) throws BestWorkBussinessException {
		RPageDto rPageDto = new RPageDto();
		try {
			if (!tUserPage.isEmpty()) {
				rPageDto.setNumber(tUserPage.getNumber());
				rPageDto.setSize(tUserPage.getSize());
				rPageDto.setTotalPages(tUserPage.getTotalPages());
				rPageDto.setTotalElements(tUserPage.getTotalElements());
			}
		} catch (Exception e) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0002, null);
		}
		return rPageDto;
	}

	private Pageable convertSearch(PageSearchUserDto pageCondition) {
		if (pageCondition.getKeyword().equals("")) {
			pageCondition.setKeyword("%%");
		} else {
			pageCondition.setKeyword("%" + pageCondition.getKeyword() + "%");
		}
		if (pageCondition.getRole().equals("")) {
			pageCondition.setRole("%%");
		}
		if (pageCondition.getStatus().equals("")) {
			pageCondition.setStatus("%%");
		}

		return PageRequest.of(Integer.parseInt(pageCondition.getPage()), Integer.parseInt(pageCondition.getSize()),
				Sort.by(pageCondition.getSortDirection(),
						convertResponseUtils.convertResponseUser(pageCondition.getSortBy())));
	}

	@Transactional(rollbackFor = { Exception.class })
	public TUser editUser(UserReqDto userReqDto, long userId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		TCompany company = tCompanyRepository.findById(findCompanyIdByUsername(userAuthRoleReq)).orElse(new TCompany());
		if (null != company.getId()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0003, null);
		}
		TUser user = tUserRepo.findById(userId).orElse(null);
		if (null == user) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0003, null);
		}

		if (userReqDto.getRole() == 1) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0003, null);
		}

		TUser tUser = new TUser();
		Set<TCompany> tCompanySet = new HashSet<>();
		if (ObjectUtils.isNotEmpty(userReqDto.getCompany())) {
			TCompany companyCurrent = companyRepository.findByCompanyId(userReqDto.getCompany());
			if (companyCurrent != null) {
				tCompanySet.add(companyCurrent);
				tUser.setCompanys(tCompanySet);
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.CPN0003, null);
			}
		} else {
			tUser.setCompanys(user.getCompanys());
		}
		tUser.setId(userId);
		tUser.setUserName(userReqDto.getUserName());
		if (null != userReqDto.getPassword()) {
			tUser.setPassword(encoder.encode(userReqDto.getPassword()));
		} else {
			tUser.setPassword(user.getPassword());
		}
		tUser.setFirstNm(userReqDto.getFirstName());
		tUser.setLastNm(userReqDto.getLastName());
		tUser.setEmail(userReqDto.getEmail());
		tUser.setTelNo(userReqDto.getTelNo());
		tUser.setIsEnable(userReqDto.getEnabled());
		if (ObjectUtils.isNotEmpty(userReqDto.getRole())) {
			TRole roleCurrent = roleRepository.findRole(userReqDto.getRole());
			if (roleCurrent != null) {
				tUser.setRole(roleCurrent);
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eR0002, null);
			}
		}

		if (null != userReqDto.getAvatar()) {
			tUser.setUserAvatar(userReqDto.getAvatar().getBytes());
		} else {
			tUser.setUserAvatar("".getBytes());
		}
		tUser.setUpdateDate(LocalDateTime.now());
		tUserRepo.save(tUser);
		return tUser;
	}

	public List<TRole> getAllRoles() {
		List<TRole> tRoleList = this.roleRepository.findAll();
		tRoleList.removeIf(tRole -> tRole.getId() == 1);
		return tRoleList;
	}

	public List<TUser> findAll() {
		return this.tUserRepo.findAll();
	}

	public Object getAllCompanyOfUser() throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		TCompany company = tCompanyRepository.findById(findCompanyIdByUsername(userAuthRoleReq)).orElse(new TCompany());
		if (null != company.getId()) {
			List<TCompany> tCompanyList = new ArrayList<>();
			tCompanyList.add(company);
			return tCompanyList;
		} else {
			return this.tCompanyRepository.findAll();
		}
	}

}
