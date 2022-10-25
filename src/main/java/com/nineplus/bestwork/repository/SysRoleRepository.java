package com.nineplus.bestwork.repository;

import com.nineplus.bestwork.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    
    SysRole findSysRoleByName(String name);

    Page<SysRole> findAllByNameContains(String name, Pageable pageable);
}
