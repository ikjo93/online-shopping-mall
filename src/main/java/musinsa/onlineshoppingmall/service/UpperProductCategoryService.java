package musinsa.onlineshoppingmall.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItem;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItems;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItems;
import musinsa.onlineshoppingmall.repository.SubProductCategoryRepository;
import musinsa.onlineshoppingmall.repository.UpperProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpperProductCategoryService {

    public static final String UNCLASSIFIED_CATEGORY = "UNCLASSIFIED";
    
    private final UpperProductCategoryRepository upperProductCategoryRepository;
    private final SubProductCategoryRepository subProductCategoryRepository;

    @Transactional(readOnly = true)
    public SubProductCategoryItems getSubCategoriesById(Long id) {
        UpperProductCategory upperProductCategory = upperProductCategoryRepository.findAllCategoriesById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상품 카테고리가 없습니다.");
        });

        List<SubProductCategoryItem> categoryItems = upperProductCategory.subProductCategoryItems();

        return new SubProductCategoryItems(categoryItems);
    }

    @Transactional(readOnly = true)
    public UpperProductCategoryItems getTotalCategories() {
        List<UpperProductCategoryItem> totalCategories = upperProductCategoryRepository.findAllCategories()
            .stream()
            .map(UpperProductCategoryItem::from)
            .collect(Collectors.toList());

        totalCategories.add(getUnclassifiedProductCategories());

        return new UpperProductCategoryItems(totalCategories);
    }

    private UpperProductCategoryItem getUnclassifiedProductCategories() {
        List<SubProductCategory> unclassifiedSubProductCategories = subProductCategoryRepository.findAllByUpperProductCategoryIsNull();
        UpperProductCategory unclassifiedProductCategory = UpperProductCategory.builder()
            .name(UNCLASSIFIED_CATEGORY).subProductCategories(unclassifiedSubProductCategories).build();

        return UpperProductCategoryItem.from(unclassifiedProductCategory);
    }

    public UpperProductCategoryItem saveCategory(String name) {
        validateDuplicateNameOfUpperProductCategories(name);

        UpperProductCategory upperProductCategory = UpperProductCategory.builder().name(name).build();

        UpperProductCategory savedUpperProductCategory = upperProductCategoryRepository.save(upperProductCategory);

        return UpperProductCategoryItem.from(savedUpperProductCategory);
    }

    private void validateDuplicateNameOfUpperProductCategories(String name) {
        Optional.ofNullable(upperProductCategoryRepository.findByName(name)).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 상위 상품 카테고리 이름입니다.");
        });
    }

    public UpperProductCategoryItem updateCategory(Long id, String name) {
        UpperProductCategory upperProductCategory = getUpperProductCategoryByIdOrThrow(id);
        validateDuplicateNameOfUpperProductCategories(name);
        upperProductCategory.validateDuplicateNameOfSubProductCategories(name);
        upperProductCategory.updateName(name);

        return UpperProductCategoryItem.from(upperProductCategory);
    }

    private UpperProductCategory getUpperProductCategoryByIdOrThrow(Long id) {
        return upperProductCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상위 상품 카테고리가 없습니다.");
        });
    }

    public void deleteCategory(Long id) {
        getUpperProductCategoryByIdOrThrow(id);
        upperProductCategoryRepository.deleteById(id);
    }
}
