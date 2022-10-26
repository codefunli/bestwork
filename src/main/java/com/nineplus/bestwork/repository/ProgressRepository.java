package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nineplus.bestwork.entity.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
	@Query(value = "select * from PROGRESS_TRACKING where project_id = :projectId", nativeQuery = true)
	List<Progress> findProgressByProjectId(@Param(value = "projectId") String projectId);

}
