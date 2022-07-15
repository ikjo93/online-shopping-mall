package musinsa.onlineshoppingmall.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import musinsa.onlineshoppingmall.dto.SubProductCategoryForm;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItem;
import musinsa.onlineshoppingmall.service.SubProductCategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubProductCategoryController {

    private final SubProductCategoryService subProductCategoryService;

    @PostMapping("/api/sub-product-categories")
    public SubProductCategoryItem create(@Valid @RequestBody SubProductCategoryForm form) {
        return subProductCategoryService.saveCategory(form.getParentCategoryId(), form.getName());
    }

}
