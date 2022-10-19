package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {

	@Query(value = " select * from post where project_id = :projectId ", nativeQuery = true)
	List<PostEntity> findPostsByProjectId(@Param(value = "projectId") String projectId);

}
