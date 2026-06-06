package com.shopHMsic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySearchModel extends BaseSearchModel {
    private String keyword;
}
