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

    public SubProductCategoryItem saveCategory(Long productCategoryId, String name) {
        ProductCategory parentCategory = getProductCategoryByIdOrThrow(productCategoryId);
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
        ProductCategory parentCategory = getProductCategoryByIdOrThrow(productCategoryId);
        subProductCategory.updateInfo(parentCategory, name);

        return SubProductCategoryItem.from(subProductCategory);
    }

    public void deleteCategory(Long id) {
        getSubProductCategoryByIdOrThrow(id);
        subProductCategoryRepository.deleteById(id);
    }

    private ProductCategory getProductCategoryByIdOrThrow(Long id) {
        return productCategoryRepository.findAllCategoriesById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상위 상품 카테고리가 없습니다.");
        });
    }

    private SubProductCategory getSubProductCategoryByIdOrThrow(Long id) {
        return subProductCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 하위 상품 카테고리가 없습니다.");
        });
    }
}
