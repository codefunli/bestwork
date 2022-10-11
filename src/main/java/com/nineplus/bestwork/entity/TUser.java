package com.nineplus.bestwork.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity(name = "TUser")
@Table(name = "T_SYS_APP_USER")
@Data
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
    private String firstNm;
    
    @Column(name = "last_name", nullable = false)
    private String lastNm;
    
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "enable", nullable = false)
    private boolean isEnable;

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
    private int loginFailedNum;

    @ManyToOne
    @JoinColumn(name = "app_role_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TRole role;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T_COMPANY_USER", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    Set<TCompany> companys;
}
