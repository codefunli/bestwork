package com.nineplus.bestwork.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nineplus.bestwork.model.enumtype.ActionType;
import com.nineplus.bestwork.model.enumtype.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SYS_ACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(name = "url", nullable = false, columnDefinition = "varchar(20)")
    private String url;

    @Column(name = "icon", nullable = false, columnDefinition = "varchar(200)")
    private String icon;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    Status status;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    ActionType actionType;

    @Column(name = "method_type", nullable = false)
    @Enumerated(EnumType.STRING)
    HttpMethod httpMethod;

    @Column(name = "created_user", nullable = false, columnDefinition = "varchar(20)")
    private String createdUser;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "updated_user", nullable = false, columnDefinition = "varchar(20)")
    private String updatedUser;

    @Column(name = "updated_date", nullable = false)
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    @JsonManagedReference
    private SysMonitorEntity sysMonitor;

}
