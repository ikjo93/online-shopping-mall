package musinsa.onlineshoppingmall.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;

@Entity
@Table(name = "upper_product_category")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpperProductCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upper_product_category_id")
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "upperProductCategory")
    private List<SubProductCategory> subProductCategories = new ArrayList<>();

    private String name;

    public List<SubProductCategoryItem> subProductCategoryItems() {
        return subProductCategories
            .stream()
            .map(SubProductCategoryItem::from)
            .collect(Collectors.toList());
    }

    public void addSubProductCategory(SubProductCategory... subProductCategories) {
        Arrays.stream(subProductCategories).forEach(subProductCategory -> {
            validateDuplicateNameOfUpperProductCategory(subProductCategory.getName());
            validateDuplicateNameOfSubProductCategories(subProductCategory.getName());
            this.subProductCategories.add(subProductCategory);
        });
    }

    private void validateDuplicateNameOfUpperProductCategory(String name) {
        if (this.name.equals(name)) {
            throw new IllegalStateException("하위 카테고리는 상위 카테고리와 동일한 이름일 수 없습니다.");
        }
    }

    private void validateDuplicateNameOfSubProductCategories(String name) {
        subProductCategories.stream()
            .filter(subProductCategory -> subProductCategory.hasSameName(name))
            .findFirst().ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 하위 카테고리 이름입니다.");
            });
    }

    public void updateName(String name) {
        validateDuplicateNameOfSubProductCategories(name);
        this.name = name;
    }

    public void removeSubProductCategory(SubProductCategory subProductCategory) {
        subProductCategories.remove(subProductCategory);
    }

    @PreRemove
    public void removeSubProductCategories() {
        subProductCategories.forEach(SubProductCategory::initUpperProductCategory);
    }
}
