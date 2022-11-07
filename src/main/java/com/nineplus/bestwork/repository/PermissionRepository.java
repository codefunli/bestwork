package com.nineplus.bestwork.repository;

import com.nineplus.bestwork.entity.SysPermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<SysPermissionEntity, Long> {
    Page<SysPermissionEntity> findBySysMonitor_NameContainingIgnoreCaseAndSysRole_RoleNameContainingIgnoreCase
            (String monitorName, String roleName, Pageable pageable);

}
