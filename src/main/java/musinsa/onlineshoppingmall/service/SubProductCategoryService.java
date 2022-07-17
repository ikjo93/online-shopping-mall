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
        UpperProductCategory upperProductCategory = getUpperProductCategoryWithSubByIdOrThrow(upperProductCategoryId);
        SubProductCategory subProductCategory = SubProductCategory.builder()
            .upperProductCategory(upperProductCategory).name(name).build();

        upperProductCategory.addSubProductCategory(subProductCategory);
        SubProductCategory savedSubProductCategory = subProductCategoryRepository.save(subProductCategory);

        return SubProductCategoryItem.from(savedSubProductCategory);
    }

    private UpperProductCategory getUpperProductCategoryWithSubByIdOrThrow(Long id) {
        return upperProductCategoryRepository.findAllCategoriesById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상위 상품 카테고리가 없습니다.");
        });
    }

    public SubProductCategory updateCategory(Long subProductCategoryId, Long upperProductCategoryId, String name) {
        SubProductCategory subProductCategory = getSubProductCategoryByIdOrThrow(subProductCategoryId);
        UpperProductCategory upperProductCategoryToUpdate = getUpperProductCategoryWithSubByIdOrThrow(upperProductCategoryId);

        subProductCategory.update(upperProductCategoryToUpdate, name);
        upperProductCategoryToUpdate.addSubProductCategory(subProductCategory);

        return subProductCategory;
    }

    public void deleteCategory(Long id) {
        subProductCategoryRepository.deleteById(id);
    }

    private SubProductCategory getSubProductCategoryByIdOrThrow(Long id) {
        return subProductCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 하위 상품 카테고리가 없습니다.");
        });
    }
}
