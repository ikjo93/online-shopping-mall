package musinsa.onlineshoppingmall.controller;

import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.dto.ProductCategoryItem;
import musinsa.onlineshoppingmall.dto.ProductCategoryCreateForm;
import musinsa.onlineshoppingmall.service.ProductCategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping("/api/product-categories")
    public ProductCategoryItem create(@RequestBody ProductCategoryCreateForm form) {
        return productCategoryService.saveCategory(form);
    }

}
