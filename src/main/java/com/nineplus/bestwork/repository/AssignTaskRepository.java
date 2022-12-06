package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nineplus.bestwork.entity.AssignTaskEntity;

public interface AssignTaskRepository extends JpaRepository<AssignTaskEntity, Long> {

	@Query(value = "SELECT * FROM ASSIGN_TASK WHERE user_id = ?1 and company_id = ?2 and project_id = ?3", nativeQuery = true)
	AssignTaskEntity findbyCondition(Long userId, Long companyId, String projectId);

	@Query(value = " select * from ASSIGN_TASK where project_id = :projectId and user_id = :userId  ", nativeQuery = true)
	AssignTaskEntity findByProjectIdAndUserId(String projectId, long userId);

	@Query(value = "select p.id as projectId, p.project_name as projectName ,can_view as canView, can_edit as canEdit from ASSIGN_TASK ast JOIN PROJECT p on p.id = ast.project_id JOIN T_SYS_APP_USER u on ast.user_id = u.id where u.id = :userId and u.user_name = :userName", nativeQuery = true)
	List<UserProjectRepository> findListProjectByUser(long userId, String userName);

	List<AssignTaskEntity> findByProjectId(String id);
}
