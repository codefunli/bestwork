package com.nineplus.bestwork.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class SysPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
	private Integer id;

	@Column(name = "can_access", nullable = false, columnDefinition = "tinyint(1)")
	private Integer canAccess;

	@Column(name = "can_ađ", nullable = false, columnDefinition = "tinyint(1)")
	private Integer canAdd;

	@Column(name = "can_edit", nullable = false, columnDefinition = "tinyint(1)")
	private Integer canEdit;

	@Column(name = "can_delete", nullable = false, columnDefinition = "tinyint(1)")
	private Integer canDelete;

	@Column(name = "created_user", nullable = false, columnDefinition = "varchar(20)")
	private String createdUser;

	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;

	@Column(name = "updated_user", nullable = false, columnDefinition = "varchar(20)")
	private String updatedUser;

	@Column(name = "updated_date", nullable = false)
	private Timestamp updatedDate;

	@Column(name = "status", nullable = false, columnDefinition = "tinyint(1)")
	private Integer status;

	@ManyToOne
	@JoinColumn(name = "monitor_id")
	private SysMonitor sysMonitor;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private SysRole sysRole;
}
