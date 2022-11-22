package com.nineplus.bestwork.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.SysMonitorEntity;

import java.util.List;

@Repository
public interface SysMonitorRepository extends JpaRepository<SysMonitorEntity, Long> {

    SysMonitorEntity findSysMonitorByName(String name);
    @Query(value = "select distinct sm.* from sys_monitor sm join sys_permission sp on sm.id = sp.monitor_id where sp.role_id = :roleId",nativeQuery = true)
    List<SysMonitorEntity> findAllMonitorByRoleId(Long roleId);
    Page<SysMonitorEntity> findAllByNameContains(String name, Pageable pageable);
}
