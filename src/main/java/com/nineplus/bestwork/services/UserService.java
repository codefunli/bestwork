package com.nineplus.bestwork.services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import com.nineplus.bestwork.dto.CompanyResDto;
import com.nineplus.bestwork.dto.CompanyUserReqDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchUserDto;
import com.nineplus.bestwork.dto.PermissionResDto;
import com.nineplus.bestwork.dto.RPageDto;
import com.nineplus.bestwork.dto.UserCompanyReqDto;
import com.nineplus.bestwork.dto.UserDetectResDto;
import com.nineplus.bestwork.dto.UserListIdDto;
import com.nineplus.bestwork.dto.UserReqDto;
import com.nineplus.bestwork.dto.UserResDto;
import com.nineplus.bestwork.dto.UserWithProjectResDto;
import com.nineplus.bestwork.entity.CompanyEntity;
import com.nineplus.bestwork.entity.RoleEntity;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.model.enumtype.Status;
import com.nineplus.bestwork.repository.AssignTaskRepository;
import com.nineplus.bestwork.repository.CompanyRepository;
import com.nineplus.bestwork.repository.RoleRepository;
import com.nineplus.bestwork.repository.UserProjectRepository;
import com.nineplus.bestwork.repository.UserRepository;
import com.nineplus.bestwork.services.impl.ScheduleServiceImpl;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.Enums.TRole;
import com.nineplus.bestwork.utils.MessageUtils;
import com.nineplus.bestwork.utils.PageUtils;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class UserService implements UserDetailsService {
	int countUserLoginFailedBlocked = 5;

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepo;

	@Autowired
	PermissionService permissionService;

	public void saveUser(UserEntity user) {
		userRepo.save(user);
	}

	public UserEntity getUserByUsername(String userName) {
		return userRepo.findByUserName(userName);
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
	CompanyRepository companyRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	MessageUtils messageUtils;

	@Autowired
	MailSenderService mailSenderService;

	@Autowired
	MailStorageService mailStorageService;

	@Autowired
	ScheduleService scheduleService;

	@Autowired
	AssignTaskRepository assignTaskRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserEntity user = userRepo.findByUserName(userName);
		if (ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("User not found");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
		return new User(user.getUserName(), user.getPassword(), authorities);
	}

	public UserResDto convertUserToUserDto(UserEntity user) {
		UserResDto dto = null;
		if (user != null) {
			dto = new UserResDto();
			dto.setId(user.getId());
			dto.setUserName(user.getUserName());
			dto.setEmail(user.getEmail());
			dto.setRole(user.getRole());
			dto.setCountLoginFailed(String.valueOf(countUserLoginFailedBlocked));
			dto.setRole(user.getRole());
			dto.setEnable(user.isEnable());
			dto.setTelNo(user.getTelNo());
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
		}
		return dto;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void registNewUser(CompanyUserReqDto companyUserReqDto, CompanyEntity tCompany, RoleEntity role) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		UserEntity newUser = new UserEntity();
		Set<CompanyEntity> companyUser = new HashSet<CompanyEntity>();
		UserCompanyReqDto newUserCompany = companyUserReqDto.getUser();
		companyUser.add(tCompany);
		newUser.setEmail(newUserCompany.getEmail());
		newUser.setUserName(newUserCompany.getUserName());
		newUser.setEnable(newUserCompany.isEnabled());
		newUser.setFirstName(newUserCompany.getFirstName());
		newUser.setLastName(newUserCompany.getLastName());
		newUser.setLoginFailedNum(0);
		newUser.setPassword(encoder.encode(newUserCompany.getPassword()));
		newUser.setTelNo(newUserCompany.getTelNo());
		newUser.setRole(role);
		newUser.setCompanys(companyUser);
		newUser.setCreateBy(userAuthRoleReq.getUsername());
		newUser.setCreateDate(LocalDateTime.now());

		userRepo.save(newUser);

		mailStorageService.saveMailRegisterUserCompToSendLater(newUserCompany.getEmail(), tCompany.getCompanyName(),
				newUserCompany.getUserName(), newUserCompany.getPassword());
		ScheduleServiceImpl.isCompleted = true;
	}

	public UserEntity getUserByCompanyId(long companyId, long role) {
		return userRepo.findUserByOrgId(companyId, role);

	}

	public long findCompanyIdByAdminUsername(UserAuthDetected userAuthRoleReq) {
		String companyAdminUserName = userAuthRoleReq.getUsername();
		long companyId = userRepo.findCompanyIdByAdminUsername(companyAdminUserName);
		return companyId;
	}

	@SuppressWarnings("unused")
	private List<UserEntity> getUsersByKeyword(String keyword) {
		return this.userRepo.getUsersByKeyword(keyword);
	}

	@Transactional(rollbackFor = { Exception.class })
	public UserEntity createUser(UserReqDto userReqDto) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		if (!(userAuthRoleReq.getIsOrgAdmin() || userAuthRoleReq.getIsSysAdmin())) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		String createUser = userAuthRoleReq.getUsername();
		RoleEntity role = new RoleEntity();
		if (ObjectUtils.isNotEmpty(userReqDto.getRole())) {
			Optional<RoleEntity> roleOptional = roleRepository.findById(userReqDto.getRole());
			if (roleOptional.isPresent()) {
				role = roleOptional.get();
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eR0002, null);
			}
		}
		Set<CompanyEntity> companies = new HashSet<>();
		CompanyEntity companyCurrent = new CompanyEntity();
		if (ObjectUtils.isNotEmpty(userReqDto.getCompany())) {
			companyCurrent = companyRepository.findByCompanyId(userReqDto.getCompany());
			if (companyCurrent != null) {
				companies.add(companyCurrent);
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.CPN0003, null);
			}
		}

		UserEntity user = new UserEntity();
		user.setCompanys(companies);
		user.setUserName(userReqDto.getUserName());
		user.setPassword(encoder.encode(userReqDto.getPassword()));
		user.setFirstName(userReqDto.getFirstName());
		user.setLastName(userReqDto.getLastName());
		user.setEmail(userReqDto.getEmail());
		user.setTelNo(userReqDto.getTelNo());
		user.setEnable(userReqDto.isEnabled());
		user.setLoginFailedNum(0);
		user.setRole(role);
		user.setCreateBy(createUser);
		user.setCreateDate(LocalDateTime.now());
		user.setDeleteFlag(0);
		if (null != userReqDto.getAvatar()) {
			user.setUserAvatar(userReqDto.getAvatar().getBytes());
		}
		UserEntity createdUser = this.userRepo.save(user);
		mailStorageService.saveMailRegisterUserCompToSendLater(userReqDto.getEmail(), companyCurrent.getCompanyName(),
				userReqDto.getUserName(), userReqDto.getPassword());
		ScheduleServiceImpl.isCompleted = true;

		return createdUser;
	}

	public List<UserEntity> findAllUsersByCompanyId(long companyId) {
		return this.userRepo.findAllUsersByCompanyId(companyId);
	}

	public UserEntity getUserById(long userId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		CompanyEntity company = companyRepository.findById(findCompanyIdByUsername(userAuthRoleReq))
				.orElse(new CompanyEntity());
		Optional<UserEntity> userOptional;
		if (null != company.getId()) {
			userOptional = this.userRepo.findUserById(userId, String.valueOf(company.getId()));
		} else {
			userOptional = this.userRepo.findUserById(userId, "%%");
		}
		return userOptional.orElse(null);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void deleteUser(UserListIdDto listId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		if (!userAuthRoleReq.getIsSysAdmin() && !userAuthRoleReq.getIsOrgAdmin()) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
		}
		try {
			List<UserEntity> tUserList = userRepo.findAllById(Arrays.asList(listId.getUserIdList()));
			if (tUserList.isEmpty() || tUserList.size() < listId.getUserIdList().length) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, listId.getUserIdList());
			}
			userRepo.deleteAllByIdInBatch(Arrays.asList(listId.getUserIdList()));
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, listId.getUserIdList());
		}
	}

	public long findCompanyIdByUsername(UserAuthDetected userAuthRoleReq) {
		String companyAdminUserName = userAuthRoleReq.getUsername();
		long companyId;
		try {
			companyId = userRepo.findCompanyIdByAdminUsername(companyAdminUserName);
		} catch (Exception e) {
			return -1;
		}
		return companyId;
	}

	public PageResDto<UserResDto> getAllUsers(PageSearchUserDto pageCondition) throws BestWorkBussinessException {
		PageResDto<UserResDto> pageResponseDto = new PageResDto<>();
		Pageable pageable = convertSearch(pageCondition);
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		CompanyEntity company = companyRepository.findById(findCompanyIdByUsername(userAuthRoleReq))
				.orElse(new CompanyEntity());
		Page<UserEntity> tUserPage;
		if (null != company.getId()) {
			tUserPage = userRepo.getAllUsers(pageable, String.valueOf(company.getId()), pageCondition);
		} else {
			tUserPage = userRepo.getAllUsers(pageable, "%%", pageCondition);
		}

		List<UserEntity> result = tUserPage.getContent().stream()
				.filter(u -> !userAuthRoleReq.getUsername().equals(u.getUserName())).collect(Collectors.toList());
		tUserPage = new PageImpl<UserEntity>(result, pageable, tUserPage.getTotalElements());
		RPageDto rPageDto = createRPageDto(tUserPage);
		List<UserResDto> userResDtoList = convertTUser(tUserPage);
		pageResponseDto.setContent(userResDtoList);
		pageResponseDto.setMetaData(rPageDto);
		return pageResponseDto;
	}

	private List<UserResDto> convertTUser(Page<UserEntity> tUserPage) throws BestWorkBussinessException {
		List<UserResDto> userResDtoList = new ArrayList<>();
		try {
			for (UserEntity tUser : tUserPage.getContent()) {
				UserResDto userResDto = new UserResDto();
				userResDto.setUserName(tUser.getUserName());
				userResDto.setEmail(tUser.getEmail());
				userResDto.setFirstName(tUser.getFirstName());
				userResDto.setLastName(tUser.getLastName());
				userResDto.setTelNo(tUser.getTelNo());
				userResDto.setRole(tUser.getRole());
				userResDto.setId(tUser.getId());
				userResDto.setEnable(tUser.isEnable());
				userResDto.setAvatar(Arrays.toString(tUser.getUserAvatar()));
				userResDtoList.add(userResDto);
			}
		} catch (Exception e) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0002, null);
		}

		return userResDtoList;
	}

	private RPageDto createRPageDto(Page<UserEntity> tUserPage) throws BestWorkBussinessException {
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
	public UserEntity editUser(UserReqDto userReqDto, long userId) throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		UserEntity curUser = this.findUserByUsername(userAuthRoleReq.getUsername());
		if (!userAuthRoleReq.getIsSysAdmin()) {
			CompanyEntity company = companyRepository.findById(findCompanyIdByUsername(userAuthRoleReq))
					.orElse(new CompanyEntity());
			if (ObjectUtils.isEmpty(company.getId())) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0003, null);
			}
			if(!(userAuthRoleReq.getIsOrgAdmin() && curUser.getCompanys().contains(company))) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
			}
		}
		UserEntity user = userRepo.findById(userId).orElse(null);
		if (null == user) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0003, null);
		}

		if (userReqDto.getRole() <= 1 || userReqDto.getRole() > TRole.values().length) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0003, null);
		}

		UserEntity userEntity = new UserEntity();
		Set<CompanyEntity> tCompanySet = new HashSet<>();
		if (ObjectUtils.isNotEmpty(userReqDto.getCompany())) {
			CompanyEntity companyCurrent = companyRepository.findByCompanyId(userReqDto.getCompany());
			if (companyCurrent != null) {
				tCompanySet.add(companyCurrent);
				userEntity.setCompanys(tCompanySet);
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.CPN0003, null);
			}
		} else {
			userEntity.setCompanys(user.getCompanys());
		}
		userEntity.setId(userId);
		if (!userReqDto.getUserName().equals(user.getUserName())) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0007, null);
		}
		userEntity.setUserName(user.getUserName());
		if (null != userReqDto.getPassword()) {
			userEntity.setPassword(encoder.encode(userReqDto.getPassword()));
		} else {
			userEntity.setPassword(user.getPassword());
		}
		userEntity.setFirstName(userReqDto.getFirstName());
		userEntity.setLastName(userReqDto.getLastName());
		userEntity.setEmail(userReqDto.getEmail());
		userEntity.setTelNo(userReqDto.getTelNo());
		userEntity.setEnable(userReqDto.isEnabled());
		userEntity.setLoginFailedNum(0);
		if (ObjectUtils.isNotEmpty(userReqDto.getRole())) {
			RoleEntity roleCurrent = roleRepository.findRole(userReqDto.getRole());
			if (roleCurrent != null) {
				userEntity.setRole(roleCurrent);
			} else {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.eR0002, null);
			}
		}

		if (null != userReqDto.getAvatar()) {
			userEntity.setUserAvatar(userReqDto.getAvatar().getBytes());
		} else {
			userEntity.setUserAvatar("".getBytes());
		}
		userEntity.setUpdateDate(LocalDateTime.now());
		userEntity.setUpdateBy(userAuthRoleReq.getUsername());
		userRepo.save(userEntity);
		return userEntity;
	}

	public List<RoleEntity> getAllRoles() {
		List<RoleEntity> roleList = this.roleRepository.findAll();
		roleList.removeIf(tRole -> tRole.getId() == 1);
		return roleList;
	}

	public List<UserEntity> findAll() {
		return this.userRepo.findAll();
	}

	public Object getAllCompanyOfUser() throws BestWorkBussinessException {
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		CompanyEntity company = companyRepository.findById(findCompanyIdByUsername(userAuthRoleReq))
				.orElse(new CompanyEntity());
		if (null != company.getId()) {
			List<CompanyEntity> companyList = new ArrayList<>();
			companyList.add(company);
			return companyList;
		} else {
			return this.companyRepository.findAll();
		}
	}

	public UserDetectResDto detectUser(String username) throws BestWorkBussinessException {
		UserEntity user = this.getUserByUsername(username);
		long userId = 0;
		if (user != null) {
			userId = user.getId();
		}
		List<UserProjectRepository> userProject = assignTaskRepository.findListProjectByUser(userId, username);
		List<UserWithProjectResDto> listAssignDto = new ArrayList<>();
		if (userProject != null) {
			for (UserProjectRepository assign : userProject) {
				UserWithProjectResDto assignDto = new UserWithProjectResDto();
				assignDto.setProjectId(assign.getProjectId());
				assignDto.setProjectName(assign.getProjectName());
				assignDto.setCanView(assign.getCanView());
				assignDto.setCanEdit(assign.getCanEdit());
				listAssignDto.add(assignDto);
			}
		}
		CompanyEntity company = companyRepository.getCompanyOfUser(userId);
		CompanyResDto companyRes = null;
		if (company != null) {
			companyRes = modelMapper.map(company, CompanyResDto.class);
		}
		UserDetectResDto userResDto = modelMapper.map(user, UserDetectResDto.class);
		if (user.getUserAvatar() != null) {
			userResDto.setAvatar(new String(user.getUserAvatar(), StandardCharsets.UTF_8));

		}
		if (companyRes != null) {
			userResDto.setCompany(companyRes);
		}
		if (listAssignDto != null) {
			userResDto.setRoleProject(listAssignDto);
		}
		List<String> roleList = new ArrayList<>();
		roleList.add(user.getRole().getRoleName());
		List<Integer> lstStt = new ArrayList<>();
		lstStt.add(Status.ACTIVE.getValue());
		Map<Long, List<PermissionResDto>> permissions = permissionService.getMapPermissions(roleList, lstStt);
		userResDto.setPermissions(permissions);
		return userResDto;
	}

	public UserEntity findUserByUsername(String username) {
		UserEntity user = new UserEntity();
		user = userRepo.findUserByUserName(username);
		return user;
	}

	public UserEntity findUserByUserId(long userId) {
		Optional<UserEntity> userOpt = userRepo.findById(userId);
		if (userOpt.isPresent()) {
			return userOpt.get();
		}
		return null;
	}

	public List<UserEntity> findUserAllowUpdPrj(String prjId) {
		List<UserEntity> userList = this.userRepo.findUserAllwUpdPrj(prjId);
		return userList;
	}

}
