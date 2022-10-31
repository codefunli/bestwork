package com.nineplus.bestwork.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

@Entity
@Table(name = "SYS_MONITOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysMonitor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
	private Long id;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
	private String name;

	@Column(name = "url", nullable = false, columnDefinition = "varchar(20)")
	private String url;

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

	@OneToMany(mappedBy = "sysMonitor")
	@JsonBackReference
	private List<SysAction> sysActions;

}
