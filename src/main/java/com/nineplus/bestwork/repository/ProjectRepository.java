package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, String> {

	@Query(value = " select id from PROJECT order by id desc limit 1 ", nativeQuery = true)
	String getLastPrjId();

	@Query(value = " delete from PROJECT where id in :id ", nativeQuery = true)
	@Modifying
	void deleteProjectById(@Param("id") List<String> id);

	@Query(value = "SELECT * FROM PROJECT WHERE project_name = :name", nativeQuery = true)
	ProjectEntity findbyProjectName(@Param("name") String name);

	@Query(value = "SELECT * FROM PROJECT WHERE id = :id", nativeQuery = true)
	ProjectEntity findbyProjectId(@Param("id") String id);

	@Query(value = "SELECT prj.id FROM PROJECT prj JOIN ASSIGN_TASK ast ON prj.id = ast.project_id where ast.company_id in ?1 ", nativeQuery = true)
	List<String> getAllPrjIdByComp(List<Long> listCompanyId);

	@Query(value = "Select tc.id as companyId, tc.company_name as companyName, tus.user_name as userName, tus.id as userId, 'false' as canView ,'false' as canEdit from T_SYS_APP_USER tus JOIN T_COMPANY_USER tcu ON tus.id = tcu.user_id JOIN T_COMPANY tc on tcu.company_id = tc.id WHERE tcu.company_id = ?1", nativeQuery = true)
	List<ProjectAssignRepository> getCompAndRoleUserByCompId(Long companyId);

	@Query(value = "Select tus.user_name as userName, ast.user_id as userId, ast.can_view as canView , ast.can_edit as canEdit from ASSIGN_TASK ast JOIN PROJECT pr ON ast.project_id = pr.id JOIN T_SYS_APP_USER tus ON tus.id = ast.user_id  WHERE ast.company_id = ?1 AND ast.project_id = ?2", nativeQuery = true)
	List<ProjectAssignRepository> getCompAndRoleUserByCompAndPrj(Long companyId, String projectId);

	@Query(value = "Select ast.company_id as companyId, tc.company_name as companyName, tus.user_name as userName, ast.user_id as userId, ast.can_view as canView , ast.can_edit as canEdit from T_COMPANY tc join ASSIGN_TASK ast ON tc.id = ast.company_id JOIN PROJECT pr ON ast.project_id = pr.id JOIN T_SYS_APP_USER tus ON tus.id = ast.user_id WHERE ast.project_id = :projectId", nativeQuery = true)
	List<ProjectAssignRepository> getCompAndRoleUserByPrj(String projectId);

	@Query(value = " select * from PROJECT where create_by = :curUsername", nativeQuery = true)
	List<ProjectEntity> findPrjCreatedByCurUser(@Param("curUsername") String curUsername);

	@Query(value = " select p.* from PROJECT p " + " join ASSIGN_TASK ast on ast.project_id = p.id"
			+ " join T_SYS_APP_USER tsau on tsau.id = ast.user_id " + " where tsau.user_name = :curUsername" + " and ( "
			+ " ast.can_view = 1 or ast.can_edit = 1 ) ", nativeQuery = true)
	List<ProjectEntity> findPrjAssignedToCurUser(@Param("curUsername") String curUsername);

	@Query(value = "select p.* from PROJECT p " + " join CONSTRUCTION c on c.project_code = p.id "
			+ " where c.id = :constructionId " + " group by p.id ", nativeQuery = true)
	ProjectEntity findByConstructionId(@Param("constructionId") long constructionId);

	@Query(value = " select * from (select p.* from PROJECT p join T_SYS_APP_USER u on p.create_by = u.user_name "
			+ " where (u.create_by = :curUsername or p.create_by = :curUsername) " + " union "
			+ " select p.* from PROJECT p join ASSIGN_TASK ast on ast.project_id = p.id "
			+ "	join T_SYS_APP_USER tsau on tsau.id = ast.user_id "
			+ " where tsau.user_name = :curUsername and (ast.can_view = 1 or ast.can_edit = 1)) as PRJ"
			+ " where ((PRJ.`project_name` like :#{#pageSearchDto.keyword}"
			+ " or PRJ.`description` like :#{#pageSearchDto.keyword})"
			+ " and PRJ.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})) ", nativeQuery = true, countQuery = " select * from (select p.* from PROJECT p join T_SYS_APP_USER u on p.create_by = u.user_name "
					+ " where (u.create_by = :curUsername or p.create_by = :curUsername) " + " union "
					+ " select p.* from PROJECT p join ASSIGN_TASK ast on ast.project_id = p.id "
					+ "	join T_SYS_APP_USER tsau on tsau.id = ast.user_id "
					+ " where tsau.user_name = :curUsername and (ast.can_view = 1 or ast.can_edit = 1)) as PRJ"
					+ " where ((PRJ.`project_name` like :#{#pageSearchDto.keyword}"
					+ " or PRJ.`description` like :#{#pageSearchDto.keyword})"
					+ " and PRJ.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})) ")
	Page<ProjectEntity> getPrjPageByOrgAdmin(@Param("curUsername") String curUsername,
			@Param("pageSearchDto") PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select p.* from PROJECT p join T_SYS_APP_USER u on p.create_by = u.user_name "
			+ " where (u.create_by in (select user.user_name from T_SYS_APP_USER user where user.create_by = :curUsername) "
			+ " or p.create_by in (select user.user_name from T_SYS_APP_USER user where user.create_by = :curUsername))"
			+ " and ((p.`project_name` like :#{#pageSearchDto.keyword}"
			+ " or p.`description` like :#{#pageSearchDto.keyword})"
			+ " and p.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})) "
			+ " group by p.id ", nativeQuery = true, countQuery = " select p.* from PROJECT p join T_SYS_APP_USER u on p.create_by = u.user_name "
					+ " where (u.create_by in (select user.user_name from T_SYS_APP_USER user where user.create_by = :curUsername) "
					+ " or p.create_by in (select user.user_name from T_SYS_APP_USER user where user.create_by = :curUsername))"
					+ " and ((p.`project_name` like :#{#pageSearchDto.keyword}"
					+ " or p.`description` like :#{#pageSearchDto.keyword})"
					+ " and p.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})) "
					+ " group by p.id ")
	Page<ProjectEntity> getPrjPageBySysAdmin(@Param("curUsername") String curUsername,
			@Param("pageSearchDto") PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select * from (select p.* from PROJECT p where p.create_by = :curUsername " + "  union "
			+ "  select p.* from PROJECT p join ASSIGN_TASK ast on ast.project_id = p.id "
			+ "  join T_SYS_APP_USER tsau on tsau.id = ast.user_id "
			+ "  where tsau.user_name = :curUsername and (ast.can_view = 1 or ast.can_edit = 1)) as PRJ "
			+ "  where (PRJ.`project_name` like :#{#pageSearchDto.keyword} "
			+ "  or PRJ.`description` like  :#{#pageSearchDto.keyword}) "
			+ "  and PRJ.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})", nativeQuery = true, 
			countQuery = " select * from (select p.* from PROJECT p where p.create_by = :curUsername "
					+ "  union " + "  select p.* from PROJECT p join ASSIGN_TASK ast on ast.project_id = p.id "
					+ "  join T_SYS_APP_USER tsau on tsau.id = ast.user_id "
					+ "  where tsau.user_name = :curUsername and (ast.can_view = 1 or ast.can_edit = 1)) as PRJ "
					+ "  where (PRJ.`project_name` like :#{#pageSearchDto.keyword} "
					+ "  or PRJ.`description` like  :#{#pageSearchDto.keyword}) "
					+ "  and PRJ.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})")
	Page<ProjectEntity> getPrjInvolvedByCurUser(@Param("curUsername") String curUsername,
			@Param("pageSearchDto") PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select * from (select p.* from PROJECT p join T_SYS_APP_USER u on p.create_by = u.user_name "
			+ " where (u.create_by = :curUsername or p.create_by = :curUsername) " + " union "
			+ " select p.* from PROJECT p join ASSIGN_TASK ast on ast.project_id = p.id "
			+ "	join T_SYS_APP_USER tsau on tsau.id = ast.user_id "
			+ " where tsau.user_name = :curUsername and (ast.can_view = 1 or ast.can_edit = 1)) as PRJ", nativeQuery = true)
	List<ProjectEntity> getPrjLstByOrgAdminUsername(String curUsername);

	@Query(value = " select p.* from PROJECT p " + " join T_SYS_APP_USER u on p.create_by = u.user_name "
			+ " where (u.create_by in (select user.user_name from T_SYS_APP_USER user where user.create_by = :curUsername) "
			+ " or p.create_by in (select user.user_name from T_SYS_APP_USER user where user.create_by = :curUsername))"
			+ " group by p.id ", nativeQuery = true)
	List<ProjectEntity> getPrjLstBySysAdminUsername(String curUsername);

}
