package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.FileStorageEntity;

@Repository
public interface StorageRepository extends JpaRepository<FileStorageEntity, String>{

	@Query(value = "select * from FILE_STORAGE where post_id = :postId", nativeQuery = true)
	List<FileStorageEntity> findAllByPostId(String postId);


}
