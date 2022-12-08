package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.AirWayBill;

@Repository
public interface AirWayBillRepository extends JpaRepository<AirWayBill, Long> {
	AirWayBill findByCode(String code);

	List<AirWayBill> findByProjectCode(String projectCode);

	@Modifying
	@Query(value = "UPDATE AIRWAY_BILL SET status = :destinationStatus WHERE id = :airWayBillId", nativeQuery = true)
	void changeStatus(long airWayBillId, int destinationStatus);

}
