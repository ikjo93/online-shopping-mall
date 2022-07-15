package musinsa.onlineshoppingmall.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.dto.ProductCategoryItem;
import musinsa.onlineshoppingmall.dto.ProductCategoryForm;
import musinsa.onlineshoppingmall.dto.ProductCategoryItems;
import musinsa.onlineshoppingmall.dto.ResponseMessage;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItems;
import musinsa.onlineshoppingmall.service.ProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping("/api/product-categories/{id}")
    public SubProductCategoryItems subCategories(@PathVariable Long id) {
        return productCategoryService.getSubCategoriesById(id);
    }

    @GetMapping("/api/product-categories")
    public ProductCategoryItems getTotalCategories() {
        return productCategoryService.getTotalCategories();
    }

    @PostMapping("/api/product-categories")
    public ProductCategoryItem create(@Valid @RequestBody ProductCategoryForm form) {
        return productCategoryService.saveCategory(form.getName());
    }

}
