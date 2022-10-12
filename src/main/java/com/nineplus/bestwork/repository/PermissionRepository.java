package com.nineplus.bestwork.repository;

import com.nineplus.bestwork.entity.SysPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<SysPermission, Long> {
    Page<SysPermission> findBySysMonitor_NameContainingIgnoreCaseAndSysRole_NameContainingIgnoreCase(String monitorName,String roleName, Pageable pageable);
}
