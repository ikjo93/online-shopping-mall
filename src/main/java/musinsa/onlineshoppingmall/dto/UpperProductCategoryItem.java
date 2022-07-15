package musinsa.onlineshoppingmall.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.domain.SubProductCategory;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpperProductCategoryItem {

    private Long id;
    private String name;
    private List<SubProductCategoryItem> subCategories;

    public static UpperProductCategoryItem from(UpperProductCategory upperProductCategory) {
        return new UpperProductCategoryItem(upperProductCategory.getId(), upperProductCategory.getName(), getSubCategories(
            upperProductCategory));
    }

    private static List<SubProductCategoryItem> getSubCategories(
        UpperProductCategory upperProductCategory) {
        List<SubProductCategory> subCategories = upperProductCategory.getSubProductCategories();
        if (subCategories == null) {
            return new ArrayList<>();
        }

        return subCategories.stream()
            .map(SubProductCategoryItem::from)
            .collect(Collectors.toList());
    }
}
