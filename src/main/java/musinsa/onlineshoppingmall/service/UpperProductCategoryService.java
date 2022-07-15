package musinsa.onlineshoppingmall.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItem;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItems;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItems;
import musinsa.onlineshoppingmall.repository.UpperProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpperProductCategoryService {

    private final UpperProductCategoryRepository upperProductCategoryRepository;

    public SubProductCategoryItems getSubCategoriesById(Long id) {
        UpperProductCategory upperProductCategory = upperProductCategoryRepository.findAllCategoriesById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상품 카테고리가 없습니다.");
        });

        List<SubProductCategoryItem> categoryItems = upperProductCategory.subProductCategoryItems();

        return new SubProductCategoryItems(categoryItems);
    }

    public UpperProductCategoryItems getTotalCategories() {
        List<UpperProductCategoryItem> totalCategories = upperProductCategoryRepository.findAllCategories()
            .stream()
            .map(UpperProductCategoryItem::from)
            .collect(Collectors.toList());

        return new UpperProductCategoryItems(totalCategories);
    }

    @Transactional
    public UpperProductCategoryItem saveCategory(String name) {
        validateDuplicateCategory(name);

        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .name(name)
            .build();

        UpperProductCategory savedUpperProductCategory = upperProductCategoryRepository.save(
            upperProductCategory);

        return UpperProductCategoryItem.from(savedUpperProductCategory);
    }

    private void validateDuplicateCategory(String name) {
        Optional.ofNullable(upperProductCategoryRepository.findByName(name)).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 카테고리입니다.");
        });
    }

    @Transactional
    public void deleteCategory(Long id) {
        upperProductCategoryRepository.deleteById(id);
    }
}
