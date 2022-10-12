package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.entity.SysPermission;
import com.nineplus.bestwork.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResRoleDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;

}
