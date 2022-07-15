package musinsa.onlineshoppingmall.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProductCategoryForm {

    @NotBlank
    private String name;

}
