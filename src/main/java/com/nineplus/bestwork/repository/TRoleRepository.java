package com.nineplus.bestwork.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nineplus.bestwork.entity.TRole;
import com.nineplus.bestwork.model.ERole;
import com.nineplus.bestwork.model.Role;

@Repository
public interface TRoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);

	@Query(value = "SELECT t FROM TRole t WHERE UPPER(t.roleName) = :role")
	TRole findRole(String role);
}
