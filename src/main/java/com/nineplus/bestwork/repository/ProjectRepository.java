package com.nineplus.bestwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.dto.PrjConditionSearchDto;
import com.nineplus.bestwork.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, String> {

	@Query(value = " select * from PROJECT where " + " (project_name like %:#{#project.keyword}% or "
			+ " description like %:#{#project.keyword}%) and status = :#{#project.status} ", nativeQuery = true)
	Page<ProjectEntity> findProjectWithCondition(@Param("project") PrjConditionSearchDto prjConditionSearchDTO,
			Pageable pageable);

	@Query(value = " select id from PROJECT order by id desc limit 1 ", nativeQuery = true)
	String getLastProjectIdString();

}
