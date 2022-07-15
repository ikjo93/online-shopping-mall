package musinsa.onlineshoppingmall.service;

import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItem;
import musinsa.onlineshoppingmall.repository.UpperProductCategoryRepository;
import musinsa.onlineshoppingmall.repository.SubProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubProductCategoryService {

    private final SubProductCategoryRepository subProductCategoryRepository;
    private final UpperProductCategoryRepository upperProductCategoryRepository;

    public SubProductCategoryItem saveCategory(Long upperProductCategoryId, String name) {
        UpperProductCategory parentCategory = getProductCategoryByIdOrThrow(upperProductCategoryId);
        parentCategory.validateDuplicateName(name);

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .parentCategory(parentCategory)
            .name(name)
            .build();

        SubProductCategory savedSubProductCategory = subProductCategoryRepository.save(subProductCategory);
        parentCategory.addSubProductCategory(savedSubProductCategory);

        return SubProductCategoryItem.from(savedSubProductCategory);
    }

    public SubProductCategoryItem updateCategory(Long id, Long productCategoryId, String name) {
        SubProductCategory subProductCategory = getSubProductCategoryByIdOrThrow(id);
        UpperProductCategory parentCategory = getProductCategoryByIdOrThrow(productCategoryId);
        subProductCategory.updateInfo(parentCategory, name);

        return SubProductCategoryItem.from(subProductCategory);
    }

    public void deleteCategory(Long id) {
        getSubProductCategoryByIdOrThrow(id);
        subProductCategoryRepository.deleteById(id);
    }

    private UpperProductCategory getProductCategoryByIdOrThrow(Long id) {
        return upperProductCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상위 상품 카테고리가 없습니다.");
        });
    }

    private SubProductCategory getSubProductCategoryByIdOrThrow(Long id) {
        return subProductCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 하위 상품 카테고리가 없습니다.");
        });
    }
}
