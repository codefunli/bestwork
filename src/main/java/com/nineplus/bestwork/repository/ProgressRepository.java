package com.nineplus.bestwork.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.ProgressEntity;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {
	List<ProgressEntity> findByConstructionId(Long constructionId);

	@Query(value = "SELECT p.id FROM PROGRESS_TRACKING p WHERE p.project_id in ?1", nativeQuery = true)
	List<Long> getAllProgressByProject(List<String> listProjectId);

	@Query(value = " delete from PROGRESS_TRACKING p where p.construction_id in :cstrtIds ", nativeQuery = true)
	@Modifying
	@Transactional
	void deleteByCstrtIdList(Long[] cstrtIds);

}
