package com.nineplus.bestwork.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SYS_MONITOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysMonitorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
	private Long id;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
	private String name;

	@Column(name = "url", nullable = false, columnDefinition = "varchar(200)")
	private String url;

	@Column(name = "icon", nullable = false, columnDefinition = "varchar(2000)")
	private String icon;

	@Column(name = "created_user", nullable = false, columnDefinition = "varchar(20)")
	private String createdUser;

	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;

	@Column(name = "updated_user", columnDefinition = "varchar(20)")
	private String updatedUser;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@OneToMany(mappedBy = "sysMonitor")
	@JsonBackReference
	private List<SysPermissionEntity> sysPermissions;

	@OneToMany(mappedBy = "sysMonitor")
	@JsonBackReference
	private List<SysActionEntity> sysActions;

}
