package com.nineplus.bestwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.TUserResponseDTO;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.repository.TUserRepository;
import com.nineplus.bestwork.utils.CommonConstants;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public TUserResponseDTO convertUserToUserDto(TUser user) {
        TUserResponseDTO dto = null;
        if (user != null) {
            dto = new TUserResponseDTO();
            dto.setId(user.getId());
            dto.setUserNm(user.getUserName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole().getRoleName());
            dto.setEnabled(user.isEnabled());
            dto.setCountLoginFailed(user.getCountLoginFailed());
            dto.setBlocked(this.isBlocked(user.getCountLoginFailed()));
            dto.setFirstNm(user.getFirstNm());
            dto.setLastNm(user.getLastNm());
            dto.setUserId(user.getUserId());
            dto.setCreateDt(user.getCreatedDt());
            dto.setUpdatedDt(user.getUpdatedDt());
            dto.setCurrentCmpnyId(user.getCurrentCpmnyId());
        }
        
        return dto;
    }
	
	

}
