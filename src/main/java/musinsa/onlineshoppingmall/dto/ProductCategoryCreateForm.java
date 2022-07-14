package musinsa.onlineshoppingmall.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCategoryCreateForm {

    @NotBlank
    private String name;

}
