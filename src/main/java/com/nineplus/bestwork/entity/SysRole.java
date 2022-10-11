package com.nineplus.bestwork.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class SysRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
	private Integer id;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
	private String name;

	@Column(name = "description", nullable = false, columnDefinition = "varchar(200)")
	private String description;

	@Column(name = "created_user", nullable = false, columnDefinition = "varchar(20)")
	private String createdUser;

	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;

	@Column(name = "updated_user", nullable = false, columnDefinition = "varchar(20)")
	private String updatedUser;

	@Column(name = "updated_date", nullable = false)
	private Timestamp updatedDate;

	@OneToMany(mappedBy = "sysRole")
	@JsonBackReference
	private List<SysUser> sysUsers;

	@OneToMany(mappedBy = "sysRole")
	@JsonBackReference
	private List<SysPermission> sysPermissions;

}
