package com.shopHMsic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoriesDto {
    @NotBlank
    private String name;

    private String description;

    private String seo;

}
