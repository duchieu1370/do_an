package com.shopHMsic.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetResponseDTO {
    private Integer code;

    private String message;

}
