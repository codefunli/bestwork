package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.MailStorageEntity;

/**
 * 
 * @author DiepTT
 *
 */
@Repository
public interface MailStorageRepository extends JpaRepository<MailStorageEntity, Long> {

	@Query(value = " select * from MAIL_STORAGE order by id ASC limit 10 ", nativeQuery = true)
	List<MailStorageEntity> findTenFirstMails();
}
