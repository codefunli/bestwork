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

}
