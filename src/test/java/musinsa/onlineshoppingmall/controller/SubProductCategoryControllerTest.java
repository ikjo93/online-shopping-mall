package musinsa.onlineshoppingmall.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.dto.SubProductCategoryItem;
import musinsa.onlineshoppingmall.service.SubProductCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("SubProductCategoryController 단위 테스트")
@WebMvcTest(SubProductCategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class SubProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubProductCategoryService subProductCategoryService;

    @Test
    @DisplayName("신규 하위 상품 카테고리 등록 요청을 처리할 수 있다.")
    void 신규_하위_상품_카테고리_등록_요청_처리() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"parentCategoryId\" : 1,\n"
            + "    \"name\" : \"스니커즈\"\n"
            + "}";

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L)
            .name("스니커즈")
            .build();

        SubProductCategoryItem responseBody = SubProductCategoryItem.from(subProductCategory);
        given(subProductCategoryService.saveCategory(1L, "스니커즈")).willReturn(responseBody);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/sub-product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['id']").value(1))
            .andExpect(jsonPath("$..['name']").value("스니커즈"));
    }
}
