package com.nineplus.bestwork.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.nineplus.bestwork.repository.TUserRepository;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
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
				pageUser = tUserRepo.findAllUsersByCompanyAdminAndCompanyId(companyId, pageable);
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
				String companyAdminUserName = userAuthRoleReq.getUsername();

				long companyId = tUserRepo.findCompanyIdByAdminUsername(companyAdminUserName);

				if (roleId > 0 & status >= 0) {
					userList = tUserRepo.findAllUSersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getRole().getId() == roleId)
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());
				} else if (roleId <= 0 & status >= 0) {
					userList = tUserRepo.findAllUSersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getIsEnable() == status).collect(Collectors.toList());
				} else if (roleId > 0 & status < 0) {
					userList = tUserRepo.findAllUSersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user))
							.filter(user -> user.getRole().getId() == roleId).collect(Collectors.toList());
				} else {
					userList = tUserRepo.findAllUSersByCompanyId(companyId).stream()
							.filter(user -> getUsersByKeyword(keyword).contains(user)).collect(Collectors.toList());
				}
			}
			pageUser = new PageImpl<>(userList, pageable, userList.size());
			return responseUtils.convertPageEntityToDTO(pageUser.map(this::convertUserToUserDto), UserResDto.class);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

	private List<TUser> getUsersByKeyword(String keyword) {
		return this.tUserRepo.getUsersByKeyword(keyword);
	}


}
