package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.UserEntity;

@Repository
public interface SysUserRepository extends JpaRepository<UserEntity, Long> {

	public UserEntity findByResetPasswordToken(String token);

	@Query(value = " select * from T_SYS_APP_USER where email = :email ", nativeQuery = true)
	public UserEntity findByEmail(@Param(value = "email") String email);
}
