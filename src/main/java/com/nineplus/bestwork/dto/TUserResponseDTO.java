package com.nineplus.bestwork.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TUserResponseDTO extends BaseDTO  {
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7969015153524943561L;

    @JsonProperty("id")
    private long id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("current_cmpny_id")
    private long currentCmpnyId;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("role")
    private String role;

    @JsonProperty("user_nm")
    private String userNm;

    @JsonProperty("email")
    private String email;

    @JsonProperty("first_nm")
    private String firstNm;

    @JsonProperty("last_nm")
    private String lastNm;

    @JsonProperty("is_deleted")
    private boolean isDeleted;

    @JsonProperty("is_blocked")
    private boolean isBlocked;

    @JsonProperty("count_login_failed")
    private int countLoginFailed;

    @JsonProperty("created_dt")
    private LocalDateTime createDt;

    @JsonProperty("updated_dt")
    private LocalDateTime updatedDt;


}
