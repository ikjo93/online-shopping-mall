package musinsa.onlineshoppingmall.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import musinsa.onlineshoppingmall.dto.ProductCategoryCreateForm;
import musinsa.onlineshoppingmall.dto.ProductCategoryItem;
import musinsa.onlineshoppingmall.service.ProductCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("ProductCategoryController 단위 테스트")
@WebMvcTest(ProductCategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductCategoryService productCategoryService;

    @Test
    @DisplayName("사용자의 신규 상품 카테고리 등록을 요청을 처리할 수 있다.")
    void 신규_상품_카테고리_등록_요청_처리() throws Exception {
        // given
        String requestBody = objectMapper.writeValueAsString(new ProductCategoryCreateForm("신발"));
        ProductCategoryItem responseBody = ProductCategoryItem.from(ProductCategory.builder()
            .id(1l)
            .name("신발")
            .build()
        );
        given(productCategoryService.saveCategory(any(ProductCategoryCreateForm.class))).willReturn(responseBody);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(responseBody)));
    }
}
