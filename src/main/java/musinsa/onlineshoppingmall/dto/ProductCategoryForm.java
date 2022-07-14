package musinsa.onlineshoppingmall.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryForm {

    @NotBlank(message = "문자 형식의 데이터를 입력해주세요.")
    private String name;

}
