package musinsa.onlineshoppingmall.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import musinsa.onlineshoppingmall.dto.ProductCategoryCreateForm;
import musinsa.onlineshoppingmall.dto.ProductCategoryItem;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItem;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItems;
import musinsa.onlineshoppingmall.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Transactional(readOnly = true)
    public SubProductCategoryItems getSubCategoriesById(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalStateException("존재하는 상품 카테고리가 없습니다.");
        });

        List<SubProductCategoryItem> categoryItems = productCategory.getSubItemCategories()
            .stream()
            .map(SubProductCategoryItem::from)
            .collect(Collectors.toList());

        return new SubProductCategoryItems(categoryItems);
    }

    @Transactional
    public ProductCategoryItem saveCategory(ProductCategoryCreateForm form) {
        validateDuplicateCategory(form.getName());
        ProductCategory productCategory = ProductCategory.builder()
            .name(form.getName())
            .build();
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);

        return ProductCategoryItem.from(savedProductCategory);
    }

    private void validateDuplicateCategory(String name) {
        Optional.ofNullable(productCategoryRepository.findByName(name)).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 카테고리입니다.");
        });
    }

}
