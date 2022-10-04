package com.nineplus.bestwork.entity;

import java.sql.Date;
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
public class SysUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
	private Integer id;

	@Column(name = "username", nullable = false, unique = true, columnDefinition = "varchar(20)")
	private String username;

	@Column(name = "password", nullable = false, columnDefinition = "varchar(100)")
	private String password;

	@Column(name = "fullname", nullable = false, columnDefinition = "varchar(50)")
	private String fullname;

	@Column(name = "phone_number", nullable = false, columnDefinition = "varchar(20)")
	private String phoneNumber;

	@Column(name = "birthday", nullable = false)
	private Date birthday;

	@Column(name = "email", nullable = false, columnDefinition = "varchar(50)")
	private String email;

	@Column(name = "address", nullable = false, columnDefinition = "varchar(50)")
	private String address;

	@Column(name = "created_user", nullable = false, columnDefinition = "varchar(20)")
	private String createdUser;

	@Column(name = "created_date", nullable = false)
	private Timestamp createdDate;

	@Column(name = "updated_user", nullable = false, columnDefinition = "varchar(20)")
	private String updatedUser;

	@Column(name = "updated_date", nullable = false)
	private Timestamp updatedDate;

	@Column(name = "status", nullable = false, columnDefinition = "tinyint(1)")
	private int status;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private SysRole sysRole;
}
