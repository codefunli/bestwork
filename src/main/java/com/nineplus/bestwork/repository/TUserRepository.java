package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TUser;

@Repository
public interface TUserRepository extends JpaRepository <TUser, Long>  {
	TUser findByUserName(String userNm);
	
	TUser findByEmail(String email);

	@Query(value = "select u.* from T_USER u JOIN T_COMPANY_USER tcu ON (u.id = tcu.user_id) where tcu.company_id = :companyId", nativeQuery = true)
    List<TUser> findAllUserByCompanyId(Long companyId);
	
	@Query(value = "select t.* from T_User t JOIN T_Org_User tou ON (t.id = tou.user_id) where tou.org_id = :orgId", nativeQuery = true)
	List<TUser> findUserByOrgId(Long orgId);
	
}
