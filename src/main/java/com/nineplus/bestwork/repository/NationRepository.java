package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.NationEntity;

@Repository
public interface NationRepository extends JpaRepository<NationEntity, Long> {

	List<NationEntity> findByIdIn(List<Long> nationIds);

}
