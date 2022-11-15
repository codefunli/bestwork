package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.PackagePost;

@Repository
public interface PackagePostRepository extends JpaRepository<PackagePost, Long> {
	List<PackagePost> findByAirWayBill(String airWayBillCode);
}
