package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.PackagePost;

@Repository
public interface PackagePostRepository extends JpaRepository<PackagePost, Long> {
	List<PackagePost> findByAirWayBill(String airWayBillCode);

	@Query(value = "SELECT path_file_server FROM FILE_STORAGE WHERE id = :fileId AND post_package_id = :packagePostId", nativeQuery = true)
	String getPathFileServer(Long packagePostId, Long fileId);
}
