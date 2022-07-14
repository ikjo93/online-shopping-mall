package musinsa.onlineshoppingmall.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryCreateForm {

    @NotBlank
    private String name;

}
