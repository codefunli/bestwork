package com.nineplus.bestwork.repository;

import com.nineplus.bestwork.entity.SysAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysActionRepository extends JpaRepository<SysAction, Long> {

    SysAction findSysActionByName(String name);
    Page<SysAction> findAllByNameContains(String name, Pageable pageable);

    @Query(value = "SELECT sa.* FROM SYS_PERMISSION sp JOIN T_SYS_APP_ROLE sr ON sp.role_id = sr.id " +
            " JOIN SYS_MONITOR sm ON sp.monitor_id = sm.id JOIN SYS_ACTION sa ON sm.id = sa.monitor_id " +
            "WHERE sr.name IN :nameList AND sa.method_type = :methodType AND sa.status = 2 ", nativeQuery = true)
    List<SysAction> findSysPermissionBySysRoleName
            (@Param("nameList") List<String> nameList, @Param("methodType") String methodType);
}
