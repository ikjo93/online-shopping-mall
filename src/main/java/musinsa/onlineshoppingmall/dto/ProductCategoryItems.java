package musinsa.onlineshoppingmall.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCategoryItems {

    private final List<ProductCategoryItem> categories;

}
