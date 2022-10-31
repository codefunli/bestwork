package com.nineplus.bestwork.entity;

import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nineplus.bestwork.model.enumtype.Status;
import lombok.*;

@Entity
@Table(name = "SYS_PERMISSION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysPermission {

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
	private SysMonitor sysMonitor;

	@ManyToOne
	@JoinColumn(name = "role_id")
	@JsonManagedReference
	private TRole sysRole;
}
