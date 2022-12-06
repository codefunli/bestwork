package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	ConstructionEntity findByConstructionName(String constructionName);

	@Query(value = "select c.* from CONSTRUCTION c " + " where c.project_code in :projectIds" + " and ( "
			+ "	c.`construction_name` like :#{#pageSearchDto.keyword} "
			+ "	or c.`description` like :#{#pageSearchDto.keyword} "
			+ "	or c.location like :#{#pageSearchDto.keyword}) "
			+ "	and c.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status}) "
			+ " group by c.id ", nativeQuery = true, countQuery = "select c.* from CONSTRUCTION c "
					+ " where c.project_code in :projectIds" + " and ( "
					+ "	c.`construction_name` like :#{#pageSearchDto.keyword} "
					+ "	or c.`description` like :#{#pageSearchDto.keyword} "
					+ "	or c.location like :#{#pageSearchDto.keyword}) "
					+ "	and c.`status` like if ( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status}) "
					+ " group by c.id ")
	Page<ConstructionEntity> findCstrtByPrjIds(List<String> projectIds, PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select * from CONSTRUCTION where id in :ids", nativeQuery = true)
	List<ConstructionEntity> findByIds(long[] ids);

	@Query(value = " select c.* from CONSTRUCTION c join PROGRESS_TRACKING pt on pt.construction_id = c.id "
			+ " where pt.id = :progressId ", nativeQuery = true)
	ConstructionEntity findByProgressId(@Param("progressId") Long progressId);
}
