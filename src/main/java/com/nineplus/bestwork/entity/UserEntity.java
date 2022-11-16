package com.nineplus.bestwork.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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

@Entity(name = "TUser")
@Table(name = "T_SYS_APP_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private long id;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "enable", nullable = false)
	private int isEnable;

	@CreationTimestamp
	@Column(name = "create_date", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createDate;

	@Column(name = "delete_flag")
	private int deleteFlag;

	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "count_login_failed")
	private Integer loginFailedNum;

	@Column(name = "tel_no")
	private String telNo;

	@Column(name = "reset_password_token", columnDefinition = "varchar(45)")
	private String resetPasswordToken;

	@Lob
	@Column(name = "user_avatar")
	private String userAvatarPath;

	@ManyToOne
	@JoinColumn(name = "role_id")
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private RoleEntity role;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_COMPANY_USER", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
	Set<CompanyEntity> companys;

	@OneToMany(mappedBy = "user")
	@JsonBackReference
	private Collection<NotificationEntity> notifications;
}
