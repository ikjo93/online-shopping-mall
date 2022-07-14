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

    @MockBean
    private ProductCategoryService productCategoryService;

    @Test
    @DisplayName("사용자의 신규 상품 카테고리 등록을 요청을 처리할 수 있다.")
    void 신규_상품_카테고리_등록_요청_처리() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"name\" : \"신발\"\n"
            + "}";
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
            .andExpect(jsonPath("$..['id']").value(1))
            .andExpect(jsonPath("$..['name']").value("신발"));
    }

    @Test
    @DisplayName("신규 상품 카테고리 등록 요청 데이터가 빈값인지 검증한다.")
    void 신규_상품_카테고리_등록_요청_데이터_빈값_검증() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"name\" : \"\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$..['status']").value("BAD_REQUEST"))
            .andExpect(jsonPath("$..['message']").value("문자 형식의 데이터를 입력해주세요."));
    }

    @Test
    @DisplayName("신규 상품 카테고리 등록 요청 데이터가 공백 문자인지 검증한다.")
    void 신규_상품_카테고리_등록_요청_데이터_공백문자_검증() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"name\" : \"   \"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$..['status']").value("BAD_REQUEST"))
            .andExpect(jsonPath("$..['message']").value("문자 형식의 데이터를 입력해주세요."));
    }

    @Test
    @DisplayName("신규 상품 카테고리 등록 요청 데이터가 올바른 형식인지 검증한다.")
    void 신규_상품_카테고리_등록_요청_데이터_형식_검증() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"name\" : \n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$..['status']").value("BAD_REQUEST"))
            .andExpect(jsonPath("$..['message']").value("요청 바디 상 데이터 항목이 잘못된 형식으로 입력되었습니다."));
    }

    @Test
    @DisplayName("신규 상품 카테고리 등록 요청 데이터가 널값인지 검증한다.")
    void 신규_상품_카테고리_등록_요청_데이터_널값_검증() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"name\" : null\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$..['status']").value("BAD_REQUEST"))
            .andExpect(jsonPath("$..['message']").value("문자 형식의 데이터를 입력해주세요."));
    }
}
