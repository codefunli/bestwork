package com.nineplus.bestwork.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.SysPermissionEntity;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PermissionRepository extends JpaRepository<SysPermissionEntity, Long> {

//    @Query(value = "SELECT * FROM T_SYS_PERMISSION WHERE role_id = ?1 AND monitor_id = ?2",nativeQuery = true)
	Page<SysPermissionEntity> findAllBySysRole_IdAndSysMonitor_Id(Long role_id, Long monitorId, Pageable pageable);

	@Query(value = "SELECT distinct t.* FROM SYS_PERMISSION t JOIN T_SYS_APP_ROLE tr on t.role_id = tr.id"
			+ " JOIN SYS_MONITOR sm ON t.monitor_id = sm.id LEFT JOIN SYS_ACTION sa ON sm.id = sa.monitor_id"
			+ " WHERE tr.name in :lstName AND t.status in :lstStt AND  sa.id = :actionId AND t.admin_id = :adminId ", nativeQuery = true)
	List<SysPermissionEntity> findAllByRoleNameAndAction(@Param("lstName") List<String> lstName,
			@Param("lstStt") List<Integer> lstStt, @Param("actionId") Long actionId, @Param("adminId") Long adminId);

	@Query(value = "SELECT distinct t.* FROM SYS_PERMISSION t JOIN T_SYS_APP_ROLE tr on t.role_id = tr.id"
			+ " JOIN SYS_MONITOR sm ON t.monitor_id = sm.id LEFT JOIN SYS_ACTION sa ON sm.id = sa.monitor_id"
			+ " WHERE tr.name in :lstName AND t.status in :lstStt ", nativeQuery = true)
	List<SysPermissionEntity> findAllBySysRole_RoleName(@Param("lstName") List<String> lstName,
														@Param("lstStt") List<Integer> lstStt);

	@Modifying
	@Transactional
	@Query(nativeQuery = true)
	void insertSystemPermissionData();

}
