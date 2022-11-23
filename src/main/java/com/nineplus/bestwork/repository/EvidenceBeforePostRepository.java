package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.EvidenceBeforePost;


@Repository
public interface EvidenceBeforePostRepository extends JpaRepository<EvidenceBeforePost, Long> {

	List<EvidenceBeforePost> findByAirWayBill(String airWayBillId);

	EvidenceBeforePost findByIdAndAirWayBill(Long evidenceBeforePostId, String airWayBillCode);
}
