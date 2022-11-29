package com.nineplus.bestwork.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nineplus.bestwork.model.enumtype.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SYS_PERMISSION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysPermissionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
	private Long id;

	@Column(name = "can_access", nullable = false, columnDefinition = "tinyint(1)")
	private Boolean canAccess;

	@Column(name = "can_add", nullable = false, columnDefinition = "tinyint(1)")
	private Boolean canAdd;

	@Column(name = "can_edit", nullable = false, columnDefinition = "tinyint(1)")
	private Boolean canEdit;

	@Column(name = "can_delete", nullable = false, columnDefinition = "tinyint(1)")
	private Boolean canDelete;

	@Column(name = "created_user", nullable = false, columnDefinition = "varchar(20)")
	private String createdUser;

	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;

	@Column(name = "updated_user", nullable = false, columnDefinition = "varchar(20)")
	private String updatedUser;

	@Column(name = "updated_date", nullable = false)
	private Timestamp updatedDate;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	Status status;

	@ManyToOne
	@JoinColumn(name = "monitor_id")
	@JsonManagedReference
	private SysMonitorEntity sysMonitor;

	@ManyToOne
	@JoinColumn(name = "role_id")
	@JsonManagedReference
	private RoleEntity sysRole;

	public Integer getStatus() {
		return status.getValue();
	}

	public void setStatus(Integer status) {
		this.status = Status.fromValue(status);
	}
}
