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

	@Query(value = "select * from FILE_STORAGE where post_id = :postId", nativeQuery = true)
	List<FileStorageEntity> findAllByPostId(String postId);

	@Query(value = " delete from FILE_STORAGE where post_id = :postId", nativeQuery = true)
	@Transactional
	@Modifying
	void deleteByPostId(String postId);

	@Query(value = "select * from FILE_STORAGE where progress_id = :progressId", nativeQuery = true)
	List<FileStorageEntity> findAllByProgressId(Long progressId);

	@Query(value = "select f.id from FILE_STORAGE f where progress_id = :progressId", nativeQuery = true)
	List<Long> getListIdFileByProgress(Long progressId);

	void deleteByIdIn(List<Long> ids);

	@Query(value = " delete from FILE_STORAGE where post_id = :postId", nativeQuery = true)
	void deleteByPostpro(String postId);

	@Query(value = "select * from FILE_STORAGE where progress_id in ?1", nativeQuery = true)
	List<FileStorageEntity> findAllByPrgListId(List<Long> ids);

	@Transactional
	@Modifying
	@Query(value = "UPDATE FILE_STORAGE SET is_choosen = :destinationStatus WHERE post_invoice_id =:postId AND id in :fileId", nativeQuery = true)
	void changeStatusInvoice(Long postId, List<Long> fileId, boolean destinationStatus);

	@Transactional
	@Modifying
	@Query(value = "UPDATE FILE_STORAGE SET is_choosen = :destinationStatus WHERE post_package_id =:postId AND id in :fileId", nativeQuery = true)
	void changeStatusPackage(Long postId, List<Long> fileId, boolean destinationStatus);

	@Query(value = "select id,name, path_file_server from FILE_STORAGE where id in ?1", nativeQuery = true)
	List<FileStorageEntity> findAllByListId(List<Long> ids);

	@Modifying
	void deleteByProgressId(long progressId);
}
