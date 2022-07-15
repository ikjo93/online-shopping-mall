package musinsa.onlineshoppingmall.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubProductCategoryForm {

    @NotNull
    private Long parentCategoryId;
    @NotBlank
    private String name;
}
