package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {

}
