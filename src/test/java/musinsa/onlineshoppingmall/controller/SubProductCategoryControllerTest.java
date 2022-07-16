package musinsa.onlineshoppingmall.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
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
    void 신규_하위카테고리_등록_요청_처리() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"스니커즈\"\n"
            + "}";

        SubProductCategoryItem responseBody = SubProductCategoryItem.from(
            SubProductCategory.builder().id(1L).name("스니커즈").build()
        );

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

    @Test
    @DisplayName("신규 하위 상품 카테고리 등록 요청 시 데이터가 올바른 형식(upperProductCategoryId : Not Null, name : Not Blank)인지 검증할 수 있다.")
    void 신규_하위카테고리_등록_요청_데이터_검증() throws Exception {
        // given
        String[] requestBodies = {
            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"\"\n"
            + "}",

            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"   \"\n"
            + "}",

            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : null\n"
            + "}",

            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"id\" : \n"
            + "}",

            "{\n"
            + "    \"upperProductCategoryId\" : null,\n"
            + "    \"name\" : \"청바지\"\n"
            + "}"
        };

        for (String requestBody : requestBodies) {
            // when
            ResultActions resultActions = mockMvc.perform(
                post("/api/sub-product-categories")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$..['status']").value("BAD_REQUEST"))
                .andExpect(jsonPath("$..['message']").value("잘못된 형식의 데이터입니다."));

        }
    }

    @Test
    @DisplayName("기존 하위 상품 카테고리 수정 요청을 처리할 수 있다.")
    void 하위카테고리_수정_요청_처리() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"스니커즈\"\n"
            + "}";

        UpperProductCategory upperProductCategoryToUpdate = UpperProductCategory.builder().id(1L).name("신발").build();
        SubProductCategory updatedSubProductCategory = SubProductCategory.builder().id(2L).name("스니커즈")
            .upperProductCategory(upperProductCategoryToUpdate).build();

        given(subProductCategoryService.updateCategory(2L, 1L, "스니커즈"))
            .willReturn(updatedSubProductCategory);

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/sub-product-categories/2")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['id']").value(2))
            .andExpect(jsonPath("$..['name']").value("스니커즈"));
    }

    @Test
    @DisplayName("기존 하위 상품 카테고리 삭제 요청을 처리할 수 있다.")
    void 하위카테고리_삭제_요청_처리() throws Exception {
        // given
        doNothing().when(subProductCategoryService).deleteCategory(2L);

        // when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/sub-product-categories/2")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        verify(subProductCategoryService, times(1)).deleteCategory(2L);
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['status']").value("OK"))
            .andExpect(jsonPath("$..['message']").value("정상적으로 삭제 처리되었습니다."));
    }
}
