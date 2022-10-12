package com.nineplus.bestwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TFileStorage;

@Repository
public interface StorageRepository extends JpaRepository<TFileStorage, Integer>{

}
