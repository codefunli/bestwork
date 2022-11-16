package com.nineplus.bestwork.repository;

import com.nineplus.bestwork.entity.SysPermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<SysPermissionEntity, Long> {

//    @Query(value = "SELECT * FROM T_SYS_PERMISSION WHERE role_id = ?1 AND monitor_id = ?2",nativeQuery = true)
    Page<SysPermissionEntity> findAllBySysRole_IdAndSysMonitor_Id
            (Long role_id,Long monitorId, Pageable pageable);

    @Query(value = "SELECT * FROM sys_permission t JOIN t_sys_app_role tr on t.role_id = tr.id WHERE tr.name in :lstName AND t.status in :lstStt ",nativeQuery = true)
    List<SysPermissionEntity> findAllBySysRole_RoleName(@Param("lstName") List<String> lstName, @Param("lstStt") List<Integer> lstStt);

}
