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
public class SysMonitor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
	private Integer id;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
	private String name;

	@Column(name = "url", nullable = false, columnDefinition = "varchar(20)")
	private String url;

	@Column(name = "parent_id", nullable = false)
	private Long parentId;

	@Column(name = "displayOrder", nullable = false)
	private Integer displayOrder;

	@Column(name = "icon", nullable = false, columnDefinition = "varchar(200)")
	private String icon;

	@Column(name = "show_access", nullable = false, columnDefinition = "tinyint(1)")
	private Integer showAccess;

	@Column(name = "show_add", nullable = false, columnDefinition = "tinyint(1)")
	private Integer showAdd;

	@Column(name = "show_edit", nullable = false, columnDefinition = "tinyint(1)")
	private Integer showEdit;

	@Column(name = "show_delete", nullable = false, columnDefinition = "tinyint(1)")
	private Integer showDelete;

	@Column(name = "created_user", nullable = false, columnDefinition = "varchar(20)")
	private String createdUser;

	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;

	@Column(name = "updated_user", nullable = false, columnDefinition = "varchar(20)")
	private String updatedUser;

	@Column(name = "updated_date", nullable = false)
	private Timestamp updatedDate;

	@OneToMany(mappedBy = "sysMonitor")
	@JsonBackReference
	private List<SysPermission> sysPermissions;

}
