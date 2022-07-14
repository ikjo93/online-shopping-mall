package musinsa.onlineshoppingmall.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import musinsa.onlineshoppingmall.dto.ProductCategoryCreateForm;
import musinsa.onlineshoppingmall.dto.ProductCategoryItem;
import musinsa.onlineshoppingmall.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

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
