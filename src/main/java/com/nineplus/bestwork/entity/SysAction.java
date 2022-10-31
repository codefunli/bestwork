package com.nineplus.bestwork.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nineplus.bestwork.model.enumtype.ActionType;
import com.nineplus.bestwork.model.enumtype.Status;
import lombok.*;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "SYS_ACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysAction {

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
    private SysMonitor sysMonitor;

}
