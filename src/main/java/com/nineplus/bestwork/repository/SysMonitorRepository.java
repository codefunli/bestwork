package com.nineplus.bestwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.SysMonitorEntity;

@Repository
public interface SysMonitorRepository extends JpaRepository<SysMonitorEntity, Long> {

    SysMonitorEntity findSysMonitorByName(String name);
    Page<SysMonitorEntity> findAllByNameContains(String name, Pageable pageable);
}
