package com.nineplus.bestwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.dto.PageSearchUserDto;
import com.nineplus.bestwork.entity.TUser;

@Repository
public interface TUserRepository extends JpaRepository<TUser, Long> {
	TUser findByUserName(String userNm);

	TUser findByEmail(String email);

	@Query(value = "select u.* from T_SYS_APP_USER u JOIN T_COMPANY_USER tcu ON (u.id = tcu.user_id) where tcu.company_id in ?1", nativeQuery = true)
	List<TUser> findAllUserByCompanyIdList(List<Long> ids);

	@Query(value = "select t.* from T_SYS_APP_USER t JOIN T_COMPANY_USER tcu ON (t.id = tcu.user_id) where tcu.company_id = :companyId and t.role_id = :role ", nativeQuery = true)
	TUser findUserByOrgId(Long companyId, Long role);

	@Query(value = "select * from T_SYS_APP_USER", countQuery = "select count(*) from T_SYS_APP_USER ", nativeQuery = true)
	Page<TUser> getPageUser(Pageable pageable);

	@Query(value = " select company_id from T_COMPANY_USER uc" + " join T_SYS_APP_USER u on u.id = uc.user_id "
			+ " where u.user_name = :username", nativeQuery = true)
	int findCompanyIdByAdminUsername(@Param("username") String companyAdminUsername);

	@Query(value = " select * from T_SYS_APP_USER u " + " join T_COMPANY_USER uc on uc.user_id = u.id "
			+ " where uc.company_id = :companyId ", countQuery = "  select count(*) from T_SYS_APP_USER u "
					+ " join T_COMPANY_USER uc on uc.user_id = u.id "
					+ " where uc.company_id = :companyId ", nativeQuery = true)
	Page<TUser> findAllUsersByCompanyId(@Param("companyId") int companyId, Pageable pageable);

	@Query(value = " select u.* from T_SYS_APP_USER u " + " where user_name like %?1% " + " or first_name like %?1% "
			+ " or last_name like %?1% " + " or email like %?1% " + " or tel_no like %?1%", nativeQuery = true)
	List<TUser> getUsersByKeyword(String keyword);

	@Query(value = " select u.* from T_SYS_APP_USER u" + " join T_COMPANY_USER uc on uc.user_id = u.id "
			+ " where uc.company_id = ?1 ", nativeQuery = true)
	List<TUser> findAllUsersByCompanyId(long companyId);

	@Query(value = " select tsau.* " + " from t_sys_app_user tsau "
			+ "         join t_company_user tcu on tsau.id = tcu.user_id "
			+ "         join t_sys_app_role tsar on tsar.id = tsau.role_id "
			+ "         join t_company tc on tc.id = tcu.company_id "
			+ " where ( tsau.email like :#{#pageCondition.keyword} or tsau.user_name like :#{#pageCondition.keyword} "
			+ " or tsau.first_name like :#{#pageCondition.keyword} or tsau.last_name like :#{#pageCondition.keyword} or "
			+ "       tsau.tel_no like :#{#pageCondition.keyword} ) "
			+ "  and tsau.enable like :#{#pageCondition.status} "
			+ "  and tsar.id like :#{#pageCondition.role} and tc.id like :companyId ", nativeQuery = true, countQuery = " select tsau.* "
					+ " from t_sys_app_user tsau " + "         join t_company_user tcu on tsau.id = tcu.user_id "
					+ "         join t_sys_app_role tsar on tsar.id = tsau.role_id "
					+ "         join t_company tc on tc.id = tcu.company_id "
					+ " where ( tsau.email like :#{#pageCondition.keyword} or tsau.user_name like :#{#pageCondition.keyword} "
					+ " or tsau.first_name like :#{#pageCondition.keyword} or tsau.last_name like :#{#pageCondition.keyword} or "
					+ "       tsau.tel_no like :#{#pageCondition.keyword} ) "
					+ "  and tsau.enable like :#{#pageCondition.status} "
					+ "  and tsar.id like :#{#pageCondition.role} and tc.id like :companyId ")
	Page<TUser> getAllUsers(Pageable pageable, @Param("companyId") String companyId,
			@Param("pageCondition") PageSearchUserDto pageCondition);

	@Query(value = " select u.* from t_sys_app_user u " + " join t_company_user tcu on u.id = tcu.user_id "
			+ " join t_company tc on tc.id = tcu.company_id "
			+ " where u.id = :userId and tc.id like :companyId ", nativeQuery = true)
	Optional<TUser> findUserById(@Param("userId") long userId, @Param("companyId") String companyId);
}
