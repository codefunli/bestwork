package com.nineplus.bestwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.entity.NotificationEntity;

/**
 * 
 * @author DiepTT
 *
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

	@Query(value = " select n.* from NOTIFICATION n where n.user_id = :userId "
			+ " and (n.title like :#{#pageSearchDto.keyword} or n.content like :#{#pageSearchDto.keyword}) "
			+ " and (n.is_read like if( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})) ", nativeQuery = true, countQuery = " select n.* from NOTIFICATION n where n.user_id = :userId "
					+ " and (n.title like :#{#pageSearchDto.keyword} or n.content like :#{#pageSearchDto.keyword}) "
					+ " and (n.is_read like if( :#{#pageSearchDto.status} = -1, '%%', :#{#pageSearchDto.status})) ")
	Page<NotificationEntity> findAllByUser(long userId, PageSearchDto pageSearchDto, Pageable pageable);

	@Query(value = " select count(n.id) from NOTIFICATION n where n.user_id = :userId"
			+ " and n.is_read = 0 ", nativeQuery = true)
	long countUnreadNotify(long userId);

}
