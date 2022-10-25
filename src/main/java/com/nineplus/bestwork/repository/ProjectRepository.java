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
	Page<ProjectEntity> findProjectWithStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select * from PROJECT where " + " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%) ", nativeQuery = true)
	Page<ProjectEntity> findProjectWithoutStatus(@Param("project") PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select id from PROJECT order by id desc limit 1 ", nativeQuery = true)
	String getLastProjectIdString();

	@Query(value = " delete from PROJECT where id in :id ", nativeQuery = true)
	@Modifying
	void deleteProjectById(@Param("id") List<String> id);
	
	@Query(value = "SELECT * FROM PROJECT WHERE project_name = :name", nativeQuery = true)
	ProjectEntity findbyProjectName(String name);
	
	@Query(value = "SELECT * FROM PROJECT WHERE id = :id", nativeQuery = true)
	ProjectEntity findbyProjectId(String id);

	@Query(value = "select tus.user_name as name, ast.user_id as userId, 0 as canView ,0 as canEdit from ASSIGN_TASK ast JOIN T_SYS_APP_USER tus ON tus.id = ast.user_id  WHERE ast.company_id = ?1 group by user_name, user_id", nativeQuery = true)
	List<ProjectAssignProjection> GetCompanyAndRoleUserByCompanyId(Long companyId);
	
	@Query(value = "select tus.user_name as name, ast.user_id as userId, 0 as canView ,0 as canEdit from ASSIGN_TASK ast JOIN T_SYS_APP_USER tus ON tus.id = ast.user_id  WHERE ast.company_id = ?1 AND ast.project_id = ?2", nativeQuery = true)
	List<ProjectAssignProjection> GetCompanyAndRoleUserByCompanyAndProject(Long companyId, String projectId);

}
