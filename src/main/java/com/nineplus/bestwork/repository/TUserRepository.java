package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TCompany;
import com.nineplus.bestwork.entity.TUser;

@Repository
public interface TUserRepository extends JpaRepository<TUser, Long> {
	TUser findByUserName(String userNm);

	TUser findByEmail(String email);

	@Query(value = "select u.* from T_SYS_APP_USER u JOIN T_COMPANY_USER tcu ON (u.id = tcu.user_id) where tcu.company_id in ?1", nativeQuery = true)
	List<TUser> findAllUserByCompanyId(List<Long> ids);

	@Query(value = "select t.* from T_SYS_APP_USER t JOIN T_COMPANY_USER tcu ON (t.id = tcu.user_id) where tcu.company_id = :companyId", nativeQuery = true)
	TUser findUserByOrgId(Long companyId);

	@Query(value = "select * from T_SYS_APP_USER", nativeQuery = true)
	Page<TUser> getPageUser(Pageable pageable);
	

	@Query(value = " select company_id from T_COMPANY_USER uc"
			+ " join T_SYS_APP_USER u on u.id = uc.user_id "
			+ " where u.user_name = :username", nativeQuery = true)
	int findCompanyIdByAdminUsername(@Param("username") String companyAdminUsername);
	
	@Query(value = " select * from T_SYS_APP_USER u "
			+ " join T_COMPANY_USER uc on uc.user_id = u.id "
			+ " where uc.company_id = :companyId "
			, nativeQuery = true)
	Page<TUser> findAllUsersByCompanyAdminAndCompanyId(@Param("companyId") int companyId, Pageable pageable);

	@Query(value = " select u.* from T_SYS_APP_USER u "
			+ " where user_name like %?1% "
			+ " or first_name like %?1% "
			+ " or last_name like %?1% "
			+ " or email like %?1% "
			+ " or tel_no like %?1%", nativeQuery =  true)
	List<TUser> getUsersByKeyword(String keyword);

	@Query(value = " select u.* from T_SYS_APP_USER u"
			+ " join T_COMPANY_USER uc on uc.user_id = u.id "
			+ " where uc.company_id = ?1 ", nativeQuery = true)
	List<TUser> findAllUSersByCompanyId(long companyId);
}
