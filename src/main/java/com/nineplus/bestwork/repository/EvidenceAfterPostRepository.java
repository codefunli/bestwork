package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.EvidenceAfterPost;

/**
 * 
 * @author TuanNA
 *
 */
@Repository
public interface EvidenceAfterPostRepository extends JpaRepository<EvidenceAfterPost, Long> {

	List<EvidenceAfterPost> findByAirWayBill(long awbId);

	EvidenceAfterPost findByIdAndAirWayBill(Long evidenceBeforePostId, String airWayBillCode);
}
