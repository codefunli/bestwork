package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.AirWayBill;

@Repository
public interface AirWayBillRepository extends JpaRepository<AirWayBill, Long> {
	AirWayBill findByCode(String code);

}