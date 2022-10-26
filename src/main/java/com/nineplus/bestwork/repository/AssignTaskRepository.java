package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nineplus.bestwork.entity.AssignTask;

public interface AssignTaskRepository extends JpaRepository<AssignTask, Long> {

	@Query(value = " select * from ASSIGN_TASK where project_id = :projectId and user_id = :userId  ", nativeQuery = true)
	AssignTask findByProjectIdAndUserId(String projectId, long userId);

}
