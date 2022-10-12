package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.SysUser;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Integer>{

	public SysUser findByResetPasswordToken(String token);

	@Query(value = " select * from sys_user where email = :email ",
			nativeQuery = true)
	public SysUser findByEmail(@Param(value = "email") String email);
}
