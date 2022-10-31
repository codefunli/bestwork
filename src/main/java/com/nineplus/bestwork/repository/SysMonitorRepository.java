package com.nineplus.bestwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.SysMonitor;

@Repository
public interface SysMonitorRepository extends JpaRepository<SysMonitor, Long> {

    SysMonitor findSysMonitorByName(String name);
    Page<SysMonitor> findAllByNameContains(String name, Pageable pageable);
}
