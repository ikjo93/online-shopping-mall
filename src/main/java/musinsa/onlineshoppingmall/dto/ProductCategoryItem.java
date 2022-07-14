package musinsa.onlineshoppingmall.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import musinsa.onlineshoppingmall.domain.ProductCategory;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCategoryItem {

    private Long id;
    private String categoryName;

    public static ProductCategoryItem from(ProductCategory productCategory) {
        return new ProductCategoryItem(productCategory.getId(), productCategory.getName());
    }
}
