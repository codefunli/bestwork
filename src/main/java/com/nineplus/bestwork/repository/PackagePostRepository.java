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
	
	@Query(value = "SELECT ft.post_package_id as postPackageId, ft.id as fileId, ft.type as type, ft.name as name , ft.path_file_server as pathFileServer "
			+ "FROM FILE_STORAGE ft " + "JOIN PACKAGE_POST pp ON ft.post_package_id = pp.id "
			+ "JOIN AIRWAY_BILL aw ON  aw.code = pp.airway_bill "
			+ "WHERE aw.id = :awbId AND ft.is_choosen = 1", nativeQuery = true)
	List<PackageFileProjection> getClearancePackageInfo(long awbId);

	PackagePost findByIdAndAirWayBill(Long postPackageId, String airWayBillCode);
}
