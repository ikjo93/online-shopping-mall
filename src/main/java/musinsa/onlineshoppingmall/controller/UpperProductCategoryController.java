package musinsa.onlineshoppingmall.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.dto.ResponseMessage;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItem;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryForm;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItems;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItems;
import musinsa.onlineshoppingmall.service.UpperProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/api/upper-product-categories/{id}")
    public UpperProductCategoryItem update(@PathVariable Long id, @Valid @RequestBody UpperProductCategoryForm form) {
        return upperProductCategoryService.updateCategory(id, form.getName());
    }

    @DeleteMapping("/api/upper-product-categories/{id}")
    public ResponseMessage delete(@PathVariable Long id) {
        upperProductCategoryService.deleteCategory(id);
        return ResponseMessage.create(HttpStatus.OK, "정상적으로 삭제 처리되었습니다.");
    }

}
