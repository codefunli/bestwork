package com.nineplus.bestwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.model.ERole;

@Repository
public interface TRoleRepository extends JpaRepository<TRole, Long> {
	Optional<TRole> findByRoleName(ERole name);

	@Query(value = "SELECT * FROM t_sys_app_role WHERE UPPER(name) = :role", nativeQuery = true)
	TRole findRole(String role);
}
