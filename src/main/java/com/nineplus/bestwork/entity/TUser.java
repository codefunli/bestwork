package com.nineplus.bestwork.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "TUser")
@Table(name = "T_SYS_APP_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TUser {

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
    private byte[] userAvatar;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TRole role;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T_COMPANY_USER", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    Set<TCompany> companys;
}
