package musinsa.onlineshoppingmall.dto.subproductcategory;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SubProductCategoryForm {
    
    private Long parentCategoryId;
    @NotBlank
    private String name;
}
