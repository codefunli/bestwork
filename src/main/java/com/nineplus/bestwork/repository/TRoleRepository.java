package com.nineplus.bestwork.repository;

import com.nineplus.bestwork.entity.TRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TRoleRepository extends JpaRepository<TRole, Long> {
	Optional<TRole> findTRoleByRoleNameContains(String roleName);

	Page<TRole> findTRolesByRoleNameContaining(String roleName, Pageable pageable);

	@Query(value = "SELECT * FROM T_SYS_APP_ROLE WHERE UPPER(name) = :role", nativeQuery = true)
	TRole findRole(String role);
}
