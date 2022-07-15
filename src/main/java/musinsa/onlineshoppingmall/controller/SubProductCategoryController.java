package musinsa.onlineshoppingmall.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.dto.ResponseMessage;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryForm;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
import musinsa.onlineshoppingmall.service.SubProductCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubProductCategoryController {

    private final SubProductCategoryService subProductCategoryService;

    @PostMapping("/api/sub-product-categories")
    public SubProductCategoryItem save(@Valid @RequestBody SubProductCategoryForm form) {
        return subProductCategoryService.saveCategory(form.getParentCategoryId(), form.getName());
    }

    @PutMapping("/api/sub-product-categories/{id}")
    public SubProductCategoryItem update(@PathVariable Long id, @Valid @RequestBody SubProductCategoryForm form) {
        return subProductCategoryService.updateCategory(id, form.getParentCategoryId(), form.getName());
    }

    @DeleteMapping("/api/sub-product-categories/{id}")
    public ResponseMessage delete(@PathVariable Long id) {
        subProductCategoryService.deleteCategory(id);
        return ResponseMessage.create(HttpStatus.OK, "정상적으로 삭제 처리되었습니다.");
    }

}
