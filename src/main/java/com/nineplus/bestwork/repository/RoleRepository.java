package com.nineplus.bestwork.repository;

import com.nineplus.bestwork.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	Optional<RoleEntity> findTRoleByRoleNameContains(String roleName);

	Page<RoleEntity> findTRolesByRoleNameContaining(String roleName, Pageable pageable);

	@Query(value = "SELECT * FROM T_SYS_APP_ROLE WHERE UPPER(name) = :role", nativeQuery = true)
	RoleEntity findRole(String role);

	@Query(value = "SELECT * FROM T_SYS_APP_ROLE WHERE id = :roleId", nativeQuery = true)
	RoleEntity findRole(Long roleId);
}
