package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.entity.ConstructionEntity;
/**
 * 
 * @author DiepTT
 *
 */
@Repository
public interface ConstructionRepository extends JpaRepository<ConstructionEntity, Long> {

	@Query(value = " select * from CONSTRUCTION where name = :constructionName ", nativeQuery = true)
	ConstructionEntity findByName(String constructionName);

	@Query(value = "select c.* from CONSTRUCTION c " 
			+ " join AWB_CONSTRUCTION awbc on awbc.construction_id = c.id "
			+ "	join AIRWAY_BILL awb on awb.id = awbc.awb_id " 
			+ "	where awb.project_id in :projectIds" 
			+ " and ( "
			+ "	c.`name` like :#{#pageSearchDto.keyword} " 
			+ "	or c.`description` like :#{#pageSearchDto.keyword} "
			+ "	or c.location like :#{#pageSearchDto.keyword} " 
			+ "	or c.create_by like :#{#pageSearchDto.keyword}) "
			+ "	and c.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status}) "
			+ " group by c.id ", nativeQuery = true,
			 countQuery = "select c.* from CONSTRUCTION c " 
						+ " join AWB_CONSTRUCTION awbc on awbc.construction_id = c.id "
						+ "	join AIRWAY_BILL awb on awb.id = awbc.awb_id " 
						+ "	where awb.project_id in :projectIds" 
						+ " and ( "
						+ "	c.`name` like :#{#pageSearchDto.keyword} " 
						+ "	or c.`description` like :#{#pageSearchDto.keyword} "
						+ "	or c.location like :#{#pageSearchDto.keyword} " 
						+ "	or c.create_by like :#{#pageSearchDto.keyword}) "
						+ "	and c.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status}) "
						+ " group by c.id " )
	Page<ConstructionEntity> findConstructionsByProjectIds(List<String> projectIds, PageSearchDto pageSearchDto, Pageable pageable);

}
