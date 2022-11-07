package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.SysUserNotFoundException;

@Service
public class SysUserService {

	@Autowired
	private SysUserRepository sysUserRepository;

	public void updateResetPasswordToken(String token, String email) throws SysUserNotFoundException {
		UserEntity sysUser = sysUserRepository.findByEmail(email);

		if (sysUser != null) {
			sysUser.setResetPasswordToken(token);
			sysUserRepository.save(sysUser);
		} else {
			throw new SysUserNotFoundException("Not found user with email " + email);
		}
	}

	public UserEntity get(String resetPasswordToken) {
		return sysUserRepository.findByResetPasswordToken(resetPasswordToken);
	}

	public void updatePassword(UserEntity sysUser, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);

		sysUser.setPassword(encodedPassword);
		sysUser.setResetPasswordToken(null);

		sysUserRepository.save(sysUser);
	}

	public UserEntity getUserByEmail(String email) {
		return this.sysUserRepository.findByEmail(email);
	}

	public List<UserEntity> getAllSysUser() {
		return this.sysUserRepository.findAll();
	}

}
