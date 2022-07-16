package musinsa.onlineshoppingmall.service;

import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
import musinsa.onlineshoppingmall.repository.SubProductCategoryRepository;
import musinsa.onlineshoppingmall.repository.UpperProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubProductCategoryService {

    private final SubProductCategoryRepository subProductCategoryRepository;
    private final UpperProductCategoryRepository upperProductCategoryRepository;

    public SubProductCategoryItem saveCategory(Long upperProductCategoryId, String name) {
        UpperProductCategory upperProductCategory = getUpperProductCategoryByIdOrThrow(upperProductCategoryId);
        upperProductCategory.validateDuplicateNameOfUpperProductCategory(name);
        upperProductCategory.validateDuplicateNameOfSubProductCategories(name);

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .upperProductCategory(upperProductCategory)
            .name(name)
            .build();

        SubProductCategory savedSubProductCategory = subProductCategoryRepository.save(subProductCategory);
        upperProductCategory.addSubProductCategory(savedSubProductCategory);

        return SubProductCategoryItem.from(savedSubProductCategory);
    }

    private UpperProductCategory getUpperProductCategoryByIdOrThrow(Long id) {
        return upperProductCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상위 상품 카테고리가 없습니다.");
        });
    }

    public SubProductCategory updateCategory(Long subProductCategoryId, Long upperProductCategoryId, String name) {
        SubProductCategory subProductCategory = getSubProductCategoryByIdOrThrow(subProductCategoryId);
        UpperProductCategory upperProductCategory = getUpperProductCategoryByIdOrThrow(upperProductCategoryId);
        subProductCategory.updateInfo(upperProductCategory, name);

        return subProductCategory;
    }

    public void deleteCategory(Long id) {
        getSubProductCategoryByIdOrThrow(id);
        subProductCategoryRepository.deleteById(id);
    }

    private SubProductCategory getSubProductCategoryByIdOrThrow(Long id) {
        return subProductCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 하위 상품 카테고리가 없습니다.");
        });
    }
}
