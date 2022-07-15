package musinsa.onlineshoppingmall.service;

import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItem;
import musinsa.onlineshoppingmall.repository.ProductCategoryRepository;
import musinsa.onlineshoppingmall.repository.SubProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubProductCategoryService {

    private final SubProductCategoryRepository subProductCategoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public SubProductCategoryItem saveCategory(Long parentCategoryId, String name) {
        ProductCategory parentCategory = getParentCategory(parentCategoryId);
        parentCategory.validateDuplicateName(name);
        SubProductCategory subProductCategory = SubProductCategory.of(parentCategory, name);
        SubProductCategory savedSubProductCategory = subProductCategoryRepository.save(subProductCategory);

        return SubProductCategoryItem.from(savedSubProductCategory);
    }

    private ProductCategory getParentCategory(Long parentCategoryId) {
        return productCategoryRepository.findAllCategoriesById(parentCategoryId).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상위 상품 카테고리가 없습니다.");
        });
    }
}