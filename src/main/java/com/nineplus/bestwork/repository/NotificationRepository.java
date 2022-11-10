package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.NotificationEntity;
/**
 * 
 * @author DiepTT
 *
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

	@Query(value = " select * from NOTIFICATION where user_id = ?1 ", nativeQuery = true)
	List<NotificationEntity> findAllByUser(long id);

}
