package musinsa.onlineshoppingmall.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import musinsa.onlineshoppingmall.domain.SubProductCategory;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCategoryItem {

    private Long id;
    private String name;
    private List<SubProductCategoryItem> subCategories;

    public static ProductCategoryItem from(ProductCategory productCategory) {
        return new ProductCategoryItem(productCategory.getId(), productCategory.getName(), getSubCategories(productCategory));
    }

    private static List<SubProductCategoryItem> getSubCategories(ProductCategory productCategory) {
        List<SubProductCategory> subCategories = productCategory.getSubItemCategories();
        if (subCategories == null) {
            return new ArrayList<>();
        }

        return subCategories.stream()
            .map(SubProductCategoryItem::from)
            .collect(Collectors.toList());
    }
}
