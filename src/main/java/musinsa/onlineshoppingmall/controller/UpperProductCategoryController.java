package musinsa.onlineshoppingmall.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.dto.UpperProductCategoryItem;
import musinsa.onlineshoppingmall.dto.UpperProductCategoryForm;
import musinsa.onlineshoppingmall.dto.UpperProductCategoryItems;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItems;
import musinsa.onlineshoppingmall.service.UpperProductCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpperProductCategoryController {

    private final UpperProductCategoryService upperProductCategoryService;

    @GetMapping("/api/upper-product-categories/{id}")
    public SubProductCategoryItems getSubCategories(@PathVariable Long id) {
        return upperProductCategoryService.getSubCategoriesById(id);
    }

    @GetMapping("/api/upper-product-categories")
    public UpperProductCategoryItems getTotalCategories() {
        return upperProductCategoryService.getTotalCategories();
    }

    @PostMapping("/api/upper-product-categories")
    public UpperProductCategoryItem save(@Valid @RequestBody UpperProductCategoryForm form) {
        return upperProductCategoryService.saveCategory(form.getName());
    }

}
