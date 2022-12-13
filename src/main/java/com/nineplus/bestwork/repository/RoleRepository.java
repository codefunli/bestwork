package com.nineplus.bestwork.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	@Value("${Role.insert}")
	static final String insertSQL = null;

	Optional<RoleEntity> findTRoleByRoleNameContains(String roleName);

	Page<RoleEntity> findTRolesByRoleNameContaining(String roleName, Pageable pageable);

	@Query(value = "SELECT * FROM T_SYS_APP_ROLE WHERE UPPER(name) = :role", nativeQuery = true)
	RoleEntity findRole(String role);

	@Query(value = "SELECT * FROM T_SYS_APP_ROLE WHERE id = :roleId", nativeQuery = true)
	RoleEntity findRole(Long roleId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true)
	void insertSystemDataRole();
}
