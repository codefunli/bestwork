package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.EvidenceBeforePost;

/**
 * 
 * @author TuanNA
 *
 */
@Repository
public interface EvidenceBeforePostRepository extends JpaRepository<EvidenceBeforePost, Long> {

	List<EvidenceBeforePost> findByAirWayBill(long airWayBillId);

	EvidenceBeforePost findByIdAndAirWayBill(Long evidenceBeforePostId, String airWayBillCode);

	@Query(value = "SELECT aw.id as awbId, ft.post_package_id as postPackageId, ft.id as fileId, ft.type as type, ft.name as name , ft.path_file_server as pathFileServer "
			+ "FROM FILE_STORAGE ft " + "JOIN PACKAGE_POST pp ON ft.post_package_id = pp.id "
			+ "JOIN AIRWAY_BILL aw ON  aw.id = pp.airway_bill"
			+ "WHERE aw.id = :awbId AND ft.is_choosen = 1", nativeQuery = true)
	List<ImageBeforeFileProjection> getClearanceImageInfo(long awbId);
}
