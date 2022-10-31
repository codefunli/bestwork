package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserListIdDto extends BaseDto {
    private static final long serialVersionUID = 6197034371540394146L;
    private Long[] userIdList;
}
