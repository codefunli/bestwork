package com.nineplus.bestwork.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.FileStorageEntity;

@Repository
public interface StorageRepository extends JpaRepository<FileStorageEntity, String>{

	@Query(value = "select * from FILE_STORAGE where post_id = :postId", nativeQuery = true)
	List<FileStorageEntity> findAllByPostId(String postId);

	@Query(value = " delete from FILE_STORAGE where post_id = :postId", nativeQuery = true)
	@Transactional
	@Modifying
	void deleteByPostId(String postId);

	@Query(value = "select * from FILE_STORAGE where progress_id = :progressId", nativeQuery = true)
	List<FileStorageEntity> findAllByProgressId(Long progressId);

}
