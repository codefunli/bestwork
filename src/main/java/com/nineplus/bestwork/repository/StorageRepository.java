package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TFileStorage;

@Repository
public interface StorageRepository extends JpaRepository<TFileStorage, Integer>{

	@Query(value = " select * from t_file_storage where project_id = :projectId", nativeQuery = true)
	List<TFileStorage> findAllByProjectId(@Param("projectId") String projectId);

}
