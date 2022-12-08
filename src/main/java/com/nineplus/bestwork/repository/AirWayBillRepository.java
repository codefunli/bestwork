package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.AirWayBill;

@Repository
public interface AirWayBillRepository extends JpaRepository<AirWayBill, Long> {
	AirWayBill findByCode(String code);

	List<AirWayBill> findByProjectCode(String projectCode);

	@Modifying
	@Query(value = "UPDATE AIRWAY_BILL SET status = :destinationStatus WHERE id = :airWayBillId", nativeQuery = true)
	void changeStatus(long airWayBillId, int destinationStatus);
  
	String findCodeById(long id);

	Integer countAllByCodeInAndStatus(List<String> lstCode,Integer status);

	@Query(value = " SELECT COUNT(DISTINCT ac.awb_id) FROM AWB_CONSTRUCTION ac JOIN CONSTRUCTION c " +
			" ON ac.construction_id = c.id JOIN ASSIGN_TASK at ON at.project_id = c.project_code " +
			" JOIN AIRWAY_BILL ab ON ac.awb_id = ab.id " +
			" WHERE at.user_id = :userId AND ( at.can_view = 1 OR at.can_edit = 1 ) AND ( ab.status = :status OR :status IS NULL ) ", nativeQuery = true)
	Integer countAwbUser(@Param("userId") long id, @Param("status") Integer status);
}
