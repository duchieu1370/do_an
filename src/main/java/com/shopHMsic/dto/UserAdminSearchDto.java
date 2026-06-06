package com.shopHMsic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminSearchDto extends BaseSearchModel {
    private String userName;
    private String province;
    private String ward;
}
