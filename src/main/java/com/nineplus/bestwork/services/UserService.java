package com.nineplus.bestwork.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.*;
import java.util.stream.Collectors;

import com.nineplus.bestwork.dto.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchUserDto;
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
			dto.setRole(user.getRole().getRoleName());
			dto.setIsEnable(user.getIsEnable());
			dto.setTelNo(user.getTelNo());
			dto.setFirstNm(user.getFirstNm());
			dto.setLastNm(user.getLastNm());
		}

		return dto;
	}

	@Transactional(rollbackFor = { Exception.class })
	public void registNewUser(UserReqDto newUser, TCompany tCompany, TRole tRole) {
		TUser newTUser = new TUser();
		Set<TCompany> tCompanyUser = new HashSet<TCompany>();
		tCompanyUser.add(tCompany);
		newTUser.setEmail(newUser.getEmail());
		newTUser.setUserName(newUser.getUserName());
		newTUser.setIsEnable(newUser.getEnabled());
		newTUser.setFirstNm(newUser.getFirstName());
		newTUser.setLastNm(newUser.getLastName());
		newTUser.setPassword(encoder.encode(newUser.getPassword()));
		newTUser.setTelNo(newUser.getTelNo());
		newTUser.setRole(tRole);
		newTUser.setCompanys(tCompanyUser);

		tUserRepo.save(newTUser);
	}

	public TUser getUserByCompanyId(long companyId) {
		return tUserRepo.findUserByOrgId(companyId);

	}

	/**
	 * 
	 * @param pageCondition condition page
	 * @return page of company follow condition
	 * @throws BestWorkBussinessException
	 */

	public PageResponseDto<UserResDto> getUserPageWithoutCondition(PageSearchUserDto pageCondition)
			throws BestWorkBussinessException {
		Page<TUser> pageUser = null;
		try {
			int pageNumber = NumberUtils.toInt(pageCondition.getPage());

			String mappedColumn = convertResponseUtils.convertResponseUser(pageCondition.getSortBy());
			Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageCondition.getSize()),
					Sort.by(pageCondition.getSortDirection(), mappedColumn));

			UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);

			if (userAuthRoleReq.getIsSysAdmin()) {
				pageUser = tUserRepo.findAll(pageable);
			} else if (userAuthRoleReq.getIsOrgAdmin()) {
				String companyAdminUsername = userAuthRoleReq.getUsername();
				int companyId = tUserRepo.findCompanyIdByAdminUsername(companyAdminUsername);
				pageUser = tUserRepo.findAllUsersByCompanyId(companyId, pageable);
			}
			return responseUtils.convertPageEntityToDTO(pageUser.map(this::convertUserToUserDto), UserResDto.class);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	public PageResponseDto<UserResDto> getUserPageWithCondition(PageSearchUserDto pageCondition)
			throws BestWorkBussinessException {
		Page<TUser> pageUser = null;
		try {
			int pageNumber = NumberUtils.toInt(pageCondition.getPage());
			String mappedColumn = convertResponseUtils.convertResponseUser(pageCondition.getSortBy());
			Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageCondition.getSize()),
					Sort.by(pageCondition.getSortDirection(), mappedColumn));

			UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
			String keyword = pageCondition.getKeyword();
			long roleId = pageCondition.getRole();
			int status = pageCondition.getStatus();
			List<TUser> userList = new ArrayList<>();
			if (userAuthRoleReq.getIsSysAdmin()) {
				long companyId = pageCondition.getCompany();

				if (companyId > 0 & roleId > 0 & status >= 0) {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getCompanys().contains(companyRepository.findById(companyId).get()))
							.filter(user -> user.getRole().getId() == roleId)
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());

				} else if (companyId <= 0 && roleId > 0 && status >= 0) {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getRole().getId() == roleId)
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());
				} else if (companyId > 0 && roleId <= 0 && status >= 0) {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getCompanys().contains(companyRepository.findById(companyId).get()))
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());
				} else if (companyId > 0 && roleId > 0 && status < 0) {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getCompanys().contains(companyRepository.findById(companyId).get()))
							.filter(user -> user.getRole().getId() == roleId).collect(Collectors.toList());

				} else if (companyId <= 0 && roleId <= 0 && status >= 0) {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());
				} else if (companyId <= 0 && roleId > 0 && status < 0) {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getRole().getId() == roleId).collect(Collectors.toList());

				} else if (companyId > 0 && roleId <= 0 && status < 0) {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getCompanys().contains(companyRepository.findById(companyId).get()))
							.collect(Collectors.toList());
				} else {
					userList = tUserRepo.findAll().stream().filter(user -> getUsersByKeyword(keyword).contains(user))
							.collect(Collectors.toList());
				}

			} else if (userAuthRoleReq.getIsOrgAdmin()) {
				long companyId = findCompanyIdByAdminUsername(userAuthRoleReq);

				if (roleId > 0 & status >= 0) {
					userList = tUserRepo.findAllUsersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getRole().getId() == roleId)
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());
				} else if (roleId <= 0 & status >= 0) {
					userList = tUserRepo.findAllUsersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());
				} else if (roleId > 0 & status < 0) {
					userList = tUserRepo.findAllUsersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getRole().getId() == roleId).collect(Collectors.toList());
				} else {
					userList = tUserRepo.findAllUsersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user)).collect(Collectors.toList());
				}
			}
			pageUser = new PageImpl<>(userList, pageable, userList.size());
			return responseUtils.convertPageEntityToDTO(pageUser.map(this::convertUserToUserDto), UserResDto.class);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	public long findCompanyIdByAdminUsername(UserAuthDetected userAuthRoleReq) {
		String companyAdminUserName = userAuthRoleReq.getUsername();
		long companyId = tUserRepo.findCompanyIdByAdminUsername(companyAdminUserName);
		return companyId;
	}

	private List<TUser> getUsersByKeyword(String keyword) {
		return this.tUserRepo.getUsersByKeyword(keyword);
	}

	public TUser createUser(UserReqDto userReqDto) throws BestWorkBussinessException {

		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		String createUser = userAuthRoleReq.getUsername();
		TRole role = new TRole();
		Optional<TRole> roleOptional = roleRepository.findById(userReqDto.getRole());
		if (roleOptional.isPresent()) {
			role = roleOptional.get();
		}
		TCompany company = tCompanyRepository.findById(findCompanyIdByAdminUsername(userAuthRoleReq)).get();
		Set<TCompany> companies = new HashSet<>();
		companies.add(company);
		TUser user = new TUser();
		user.setUserName(userReqDto.getUserName());
		user.setPassword(encoder.encode(userReqDto.getPassword()));
		user.setFirstNm(userReqDto.getFirstName());
		user.setLastNm(userReqDto.getLastName());
		user.setEmail(userReqDto.getEmail());
		user.setTelNo(userReqDto.getTelNo());
		user.setIsEnable(userReqDto.getEnabled());
		user.setRole(role);
		user.setCreateBy(createUser);
		user.setCreateDate(LocalDateTime.now());
		user.setDeleteFlag(0);
		user.setCompanys(companies);

		return this.tUserRepo.save(user);
	}

	public List<TUser> findAllUsersByCompanyId(long companyId) {
		return this.tUserRepo.findAllUsersByCompanyId(companyId);
	}

	public TUser getUserById(long userId) {
		Optional<TUser> userOptional = this.tUserRepo.findById(userId);
		if (!userOptional.isPresent()) {
			return null;
		}
		return userOptional.get();
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
}
