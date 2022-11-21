package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nineplus.bestwork.entity.ProgressEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {
	@Query(value = "select * from PROGRESS_TRACKING where construction_id = :constructionId", nativeQuery = true)
	List<ProgressEntity> findProgressByCstrtId(@Param(value = "constructionId") Long constructionId);

	@Modifying
	@Query(value = "DELETE from PROGRESS_TRACKING p where p.id in ?1", nativeQuery = true)
	void delProgressWithId(List<Long> ids);

	@Query(value = "SELECT p.id FROM PROGRESS_TRACKING p WHERE p.project_id in ?1", nativeQuery = true)
	List<Long> getAllProgressByProject(List<String> listProjectId);


}
