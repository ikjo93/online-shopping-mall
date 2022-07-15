package musinsa.onlineshoppingmall.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubProductCategoryForm {

    @NotNull
    private Long parentCategoryId;
    @NotBlank
    private String name;
}
