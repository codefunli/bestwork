package com.nineplus.bestwork.repository;

import java.util.List;

import javax.transaction.Transactional;

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
@Transactional
public interface ProjectRepository extends JpaRepository<ProjectEntity, String> {

	@Query(value = " select * from PROJECT where " + " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%) and status = :#{#project.status} ", nativeQuery = true)
	Page<ProjectEntity> findProjectForAdminWithStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select * from PROJECT where " + " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%) ", nativeQuery = true)
	Page<ProjectEntity> findProjectForAdminWithoutStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable);

	// Get project for each company creator
	@Query(value = " select * from PROJECT where create_by = :userCreated AND"
			+ " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%) and status = :#{#project.status} ", nativeQuery = true)
	Page<ProjectEntity> findProjectForCompanyWithStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable,
			String userCreated);

	// Get project for each company creator
	@Query(value = " select * from PROJECT where create_by = :userCreated AND"
			+ " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%) ", nativeQuery = true)
	Page<ProjectEntity> findProjectForCompanyWithoutStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable,
			String userCreated);

	// Get project for user be assign
	@Query(value = "select prj.id, prj.project_name, prj.description, prj.project_type,"
			+ " prj.notification_flag,prj.is_paid, prj.status, prj.create_date, prj.update_date, prj.start_date,prj.create_by, prj.update_by from PROJECT prj"
			+ " LEFT join ASSIGN_TASK ast on prj.id =  ast.project_id "
			+ " LEFT join T_SYS_APP_USER tus on ast.user_id = tus.id " + "where user_name = :user AND"
			+ " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%) and status = :#{#project.status} ", nativeQuery = true)
	Page<ProjectEntity> findAssignToUserWithStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable,
			String user);

	// Get project for user be assign
	@Query(value = "select prj.id, prj.project_name, prj.description, prj.project_type,"
			+ " prj.notification_flag,prj.is_paid, prj.status, prj.create_date, prj.update_date, prj.start_date,prj.create_by, prj.update_by from PROJECT prj"
			+ " LEFT join ASSIGN_TASK ast on prj.id =  ast.project_id "
			+ " LEFT join T_SYS_APP_USER tus on ast.user_id = tus.id " + "where user_name = :user AND"
			+ " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%)", nativeQuery = true)
	Page<ProjectEntity> findAssignToUserWithOutStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable,
			String user);

	@Query(value = " select id from PROJECT order by id desc limit 1 ", nativeQuery = true)
	String getLastProjectIdString();

	@Query(value = " delete from PROJECT where id in :id ", nativeQuery = true)
	@Modifying
	void deleteProjectById(@Param("id") List<String> id);

	@Query(value = "SELECT * FROM PROJECT WHERE project_name = :name", nativeQuery = true)
	ProjectEntity findbyProjectName(String name);

	@Query(value = "SELECT * FROM PROJECT WHERE id = :id", nativeQuery = true)
	ProjectEntity findbyProjectId(String id);

	@Query(value = "SELECT prj.id FROM PROJECT prj JOIN ASSIGN_TASK ast ON prj.id = ast.project_id where ast.company_id in ?1 ", nativeQuery = true)
	List<String> getAllProjectIdByCompany(List<Long> listCompanyId);

	@Query(value = "Select tc.id as companyId, tc.company_name as companyName, tus.user_name as userName, tus.id as userId, 'false' as canView ,'false' as canEdit from T_SYS_APP_USER tus JOIN T_COMPANY_USER tcu ON tus.id = tcu.user_id JOIN T_COMPANY tc on tcu.company_id = tc.id WHERE tcu.company_id = ?1", nativeQuery = true)
	List<ProjectAssignRepository> GetCompanyAndRoleUserByCompanyId(Long companyId);

	@Query(value = "Select tus.user_name as userName, ast.user_id as userId, ast.can_view as canView , ast.can_edit as canEdit from ASSIGN_TASK ast JOIN PROJECT pr ON ast.project_id = pr.id JOIN T_SYS_APP_USER tus ON tus.id = ast.user_id  WHERE ast.company_id = ?1 AND ast.project_id = ?2", nativeQuery = true)
	List<ProjectAssignRepository> GetCompanyAndRoleUserByCompanyAndProject(Long companyId, String projectId);

	@Query(value = "Select ast.company_id as companyId, tc.company_name as companyName, tus.user_name as userName, ast.user_id as userId, ast.can_view as canView , ast.can_edit as canEdit from T_COMPANY tc join ASSIGN_TASK ast ON tc.id = ast.company_id JOIN PROJECT pr ON ast.project_id = pr.id JOIN T_SYS_APP_USER tus ON tus.id = ast.user_id WHERE ast.project_id = :projectId", nativeQuery = true)
	List<ProjectAssignRepository> GetCompanyAndRoleUserByProject(String projectId);

	@Query(value = " select * from PROJECT where create_by = :curUsername", nativeQuery = true )
	List<ProjectEntity> findProjectsBeingCreatedByCurrentUser(String curUsername);

	@Query(value = " select * from PROJECT p "
			+ " join ASSIGN_TASK ast on ast.project_id = p.id"
			+ " join T_SYS_APP_USER tsau on tsau.id = ast.user_id "
			+ " where tsau.user_name = :curUsername"
			+ " and ( "
			+ " ast.can_view = 1 or ast.can_edit = 1 ) ", nativeQuery = true )
	List<ProjectEntity> findProjectsBeingAssignedToCurrentUser(String curUsername);

	@Query(value = "select * from PROJECT p "
			+ " join AIRWAY_BILL awb on awb.project_code = p.id "
			+ " join AWB_CONSTRUCTION awbc on awbc.awb_id = awb.id"
			+ " where awbc.construction_id = :constructionId "
			+ " group by p.id ", nativeQuery = true )
	ProjectEntity findByConstructionId(long constructionId);



}
