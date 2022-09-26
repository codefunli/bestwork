package com.nineplus.bestwork.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity(name = "UserEntity")
@Table(name = "t_user")
@Data
public class TUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, precision = 19)
    private long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "cur_company_id", nullable = false)
    private Long currentCpmnyId;

    @Column(name = "user_name", nullable = false)
    private String userName;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "first_nm", nullable = false)
    private String firstNm;
    
    @Column(name = "last_nm", nullable = false)
    private String lastNm;
    
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "enabled", nullable = false, length = 3)
    private boolean enabled;

    @CreationTimestamp
    @Column(name = "crt_dt", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdDt;
    
    @Column(name = "tel_no", nullable = false)
    private String telNo;
    
    @Column(name = "is_deleted", nullable = false, length = 3)
    private boolean isDeleted;

    @Column(name = "count_login_failed", nullable = false)
    private int countLoginFailed;
   
    @UpdateTimestamp
    @Column(name = "update_dt", nullable = false)
    private LocalDateTime updatedDt;

    @Column(name = "token", nullable = false)
    private String token;
    
    @OneToOne
    @JoinColumn(name = "t_role",  referencedColumnName = "id")
    private TRole role;
}
