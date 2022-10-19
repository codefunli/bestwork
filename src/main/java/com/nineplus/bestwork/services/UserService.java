package com.nineplus.bestwork.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

import com.nineplus.bestwork.dto.PageResponseDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.UserReqDto;
import com.nineplus.bestwork.dto.UserResDto;
import com.nineplus.bestwork.entity.TCompany;
import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.repository.TUserRepository;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.PageUtils;

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
    BCryptPasswordEncoder encoder;
	
	@Autowired
	ConvertResponseUtils convertResponseUtils;

	@Autowired
	PageUtils responseUtils;
	
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
	
	
	
	@Transactional(rollbackFor = {Exception.class})
	public void registNewUser(UserReqDto newUser, TCompany tCompany, TRole tRole ) {
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
	public PageResponseDto<UserResDto> getUserPage(PageSearchDto pageCondition)
			throws BestWorkBussinessException {
		Page<TUser> pageUser;
		try {
			int pageNumber = NumberUtils.toInt(pageCondition.getPage());

			String mappedColumn = convertResponseUtils.convertResponseUser(pageCondition.getSortBy());
			Pageable pageable = PageRequest.of(pageNumber, Integer.parseInt(pageCondition.getSize()),
					Sort.by(pageCondition.getSortDirection(), mappedColumn));
			pageUser = tUserRepo.getPageUser(pageable);
			return responseUtils.convertPageEntityToDTO(pageUser.map(this::convertUserToUserDto), UserResDto.class);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0003, null);
		}
	}

}
