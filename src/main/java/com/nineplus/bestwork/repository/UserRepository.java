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
import com.nineplus.bestwork.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByUserName(String userNm);

	UserEntity findByEmail(String email);

	@Query(value = "select u.* from T_SYS_APP_USER u JOIN T_COMPANY_USER tcu ON (u.id = tcu.user_id) where tcu.company_id in ?1", nativeQuery = true)
	List<UserEntity> findAllUserByCompanyIdList(List<Long> ids);

	@Query(value = "select t.* from T_SYS_APP_USER t JOIN T_COMPANY_USER tcu ON (t.id = tcu.user_id) where tcu.company_id = :companyId and t.role_id = :role ", nativeQuery = true)
	UserEntity findUserByOrgId(Long companyId, Long role);

	@Query(value = " select company_id from T_COMPANY_USER uc" + " join T_SYS_APP_USER u on u.id = uc.user_id "
			+ " where u.user_name = :username", nativeQuery = true)
	int findCompanyIdByAdminUsername(@Param("username") String companyAdminUsername);

	@Query(value = " select * from T_SYS_APP_USER u " + " join T_COMPANY_USER uc on uc.user_id = u.id "
			+ " where uc.company_id = :companyId ", countQuery = "  select count(*) from T_SYS_APP_USER u "
					+ " join T_COMPANY_USER uc on uc.user_id = u.id "
					+ " where uc.company_id = :companyId ", nativeQuery = true)
	Page<UserEntity> findAllUsersByCompanyId(@Param("companyId") int companyId, Pageable pageable);

	@Query(value = " select u.* from T_SYS_APP_USER u " + " where user_name like %?1% " + " or first_name like %?1% "
			+ " or last_name like %?1% " + " or email like %?1% " + " or tel_no like %?1%", nativeQuery = true)
	List<UserEntity> getUsersByKeyword(String keyword);

	@Query(value = " select tsau.* " + " from T_SYS_APP_USER tsau "
			+ " join T_COMPANY_USER tcu on tsau.id = tcu.user_id "
			+ " join T_SYS_APP_ROLE tsar on tsar.id = tsau.role_id " + " join T_COMPANY tc on tc.id = tcu.company_id "
			+ " where ( tsau.email like :#{#pageCondition.keyword} or tsau.user_name like :#{#pageCondition.keyword} "
			+ " or tsau.first_name like :#{#pageCondition.keyword} or tsau.last_name like :#{#pageCondition.keyword} or "
			+ " tsau.tel_no like :#{#pageCondition.keyword} ) " + " and tsau.enable like :#{#pageCondition.status} "
			+ " and tsar.id like :#{#pageCondition.role} and tc.id like :companyId ", nativeQuery = true, countQuery = " select tsau.* "
					+ " from T_SYS_APP_USER tsau " + " join T_COMPANY_USER tcu on tsau.id = tcu.user_id "
					+ " join T_SYS_APP_ROLE tsar on tsar.id = tsau.role_id "
					+ " join T_COMPANY tc on tc.id = tcu.company_id "
					+ " where ( tsau.email like :#{#pageCondition.keyword} or tsau.user_name like :#{#pageCondition.keyword} "
					+ " or tsau.first_name like :#{#pageCondition.keyword} or tsau.last_name like :#{#pageCondition.keyword} or "
					+ " tsau.tel_no like :#{#pageCondition.keyword} ) "
					+ " and tsau.enable like :#{#pageCondition.status} "
					+ " and tsar.id like :#{#pageCondition.role} and tc.id like :companyId ")
	Page<UserEntity> getAllUsers(Pageable pageable, @Param("companyId") String companyId,
			@Param("pageCondition") PageSearchUserDto pageCondition);

	@Query(value = " select u.* from T_SYS_APP_USER u " + " join T_COMPANY_USER tcu on u.id = tcu.user_id "
			+ " join T_COMPANY tc on tc.id = tcu.company_id "
			+ " where u.id = :userId and tc.id like :companyId ", nativeQuery = true)
	Optional<UserEntity> findUserById(@Param("userId") long userId, @Param("companyId") String companyId);

	@Query(value = " select * from T_SYS_APP_USER where user_name = ?1 ", nativeQuery = true)
	UserEntity findUserByUserName(String username);

	@Query(value = " select * from T_SYS_APP_USER u" + " join ASSIGN_TASK t on t.user_id = u.id "
			+ " where t.project_id = :prjId and t.can_edit = 1 ", nativeQuery = true)
	List<UserEntity> findUserAllwUpdPrj(String prjId);
}
