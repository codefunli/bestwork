package com.nineplus.bestwork.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nineplus.bestwork.entity.AssignTask;

public interface AssignTaskRepository extends JpaRepository<AssignTask, Long> {
	@Query(value = "SELECT * FROM ASSIGN_TASK WHERE company_id = :id", nativeQuery = true)
	List<AssignTask> findbyCompanyId(Long id);
	
	@Query(value = "SELECT * FROM ASSIGN_TASK WHERE user_id = ?1 and company_id = ?2 and project_id = ?3", nativeQuery = true)
	AssignTask findbyCondition(Long userId, Long companyId, String projectId);

	@Query(value = " select * from ASSIGN_TASK where project_id = :projectId and user_id = :userId  ", nativeQuery = true)
	AssignTask findByProjectIdAndUserId(String projectId, long userId);

}
