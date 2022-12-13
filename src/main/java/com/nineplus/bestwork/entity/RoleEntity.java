package com.nineplus.bestwork.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "TRole")
@Table(name = "T_SYS_APP_ROLE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity implements Serializable,Cloneable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private Long id;

	@Column(name = "name", nullable = false)
	private String roleName;

	@Column(name = "description", nullable = false)
	private String description;

	@CreationTimestamp
	@Column(name = "create_date", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createDate;

	@UpdateTimestamp
	@Column(name = "update_date", nullable = false)
	private LocalDateTime updateDate;

	@Column(name = "delete_flag")
	private Integer deleteFlag;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonBackReference
	private Collection<UserEntity> users;

	@OneToMany(mappedBy = "sysRole", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonBackReference
	private Collection<SysPermissionEntity> sysPermissions;

	@Override
	public RoleEntity clone() {
		try {
			// TODO: copy mutable state here, so the clone can't change the internals of the original
			return (RoleEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
