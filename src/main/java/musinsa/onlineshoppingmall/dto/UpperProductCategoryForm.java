package musinsa.onlineshoppingmall.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpperProductCategoryForm {

    @NotBlank
    private String name;

}
