package musinsa.onlineshoppingmall.dto.subproductcategory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import musinsa.onlineshoppingmall.domain.SubProductCategory;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SubProductCategoryItem {

    private Long id;
    private String name;

    public static SubProductCategoryItem from(SubProductCategory subProductCategory) {
        return new SubProductCategoryItem(subProductCategory.getId(), subProductCategory.getName());
    }

}
