package com.nineplus.bestwork.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.FileStorageEntity;

@Repository
public interface StorageRepository extends JpaRepository<FileStorageEntity, Long> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE FILE_STORAGE SET is_choosen = :destinationStatus WHERE post_invoice_id =:postId AND id in :fileId", nativeQuery = true)
	void changeStatusInvoice(Long postId, List<Long> fileId, boolean destinationStatus);

	@Transactional
	@Modifying
	@Query(value = "UPDATE FILE_STORAGE SET is_choosen = :destinationStatus WHERE post_package_id =:postId AND id in :fileId", nativeQuery = true)
	void changeStatusPackage(Long postId, List<Long> fileId, boolean destinationStatus);

	@Modifying
	void deleteByProgressId(long progressId);

	@Modifying
	void deleteByConstructionId(long constructionId);

	@Query(value = " select path_file_server from FILE_STORAGE where construction_id = :constructionId", nativeQuery = true)
	List<String> findAllPathsByCstrtId(long constructionId);

	@Query(value = " select path_file_server from FILE_STORAGE where progress_id = :progressId", nativeQuery = true)
	List<String> findAllPathsByProgressId(long progressId);

	@Query(value = " delete from FILE_STORAGE where progress_id in :progressIds ", nativeQuery = true)
	@Modifying
	@Transactional
	void deleteByProgressIds(List<Long> progressIds);

	@Query(value = " delete from FILE_STORAGE where construction_id in :constructionIds ", nativeQuery = true)
	@Modifying
	@Transactional
	void deleteByConstructionIds(List<Long> constructionIds);
}
