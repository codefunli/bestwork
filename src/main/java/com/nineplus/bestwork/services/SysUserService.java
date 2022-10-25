package com.nineplus.bestwork.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.SysUserNotFoundException;
import com.nineplus.bestwork.repository.SysUserRepository;

@Service
public class SysUserService {

	@Autowired
	private SysUserRepository sysUserRepository;

	public void updateResetPasswordToken(String token, String email) throws SysUserNotFoundException {
		TUser sysUser = sysUserRepository.findByEmail(email);

		if (sysUser != null) {
			sysUser.setResetPasswordToken(token);
			sysUserRepository.save(sysUser);
		} else {
			throw new SysUserNotFoundException("Not found user with email " + email);
		}
	}

	public TUser get(String resetPasswordToken) {
		return sysUserRepository.findByResetPasswordToken(resetPasswordToken);
	}

	public void updatePassword(TUser sysUser, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);

		sysUser.setPassword(encodedPassword);
		sysUser.setResetPasswordToken(null);

		sysUserRepository.save(sysUser);
	}

	public TUser getUserByEmail(String email) {
		return this.sysUserRepository.findByEmail(email);
	}

	public List<TUser> getAllSysUser() {
		return this.sysUserRepository.findAll();
	}

}
