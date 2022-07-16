package musinsa.onlineshoppingmall.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItems;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItem;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItems;
import musinsa.onlineshoppingmall.service.UpperProductCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("UpperProductCategoryController 단위 테스트")
@WebMvcTest(UpperProductCategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UpperProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpperProductCategoryService upperProductCategoryService;

    @Test
    @DisplayName("상위 상품 카테고리 식별자로 하위 상품 카테고리 조회 요청을 처리할 수 있다.")
    void 상위카테고리_식별자로_하위카테고리_조회_요청_처리() throws Exception {
        // given
        SubProductCategoryItem subProductCategoryItem1 = SubProductCategoryItem.from(SubProductCategory.builder().id(1L).name("팔찌").build());
        SubProductCategoryItem subProductCategoryItem2 = SubProductCategoryItem.from(SubProductCategory.builder().id(2L).name("목걸이").build());

        given(upperProductCategoryService.getSubCategoriesById(1L))
            .willReturn(new SubProductCategoryItems(List.of(subProductCategoryItem1, subProductCategoryItem2)));

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/upper-product-categories/1")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.subCategories[0].id").value(1))
            .andExpect(jsonPath("$.subCategories[0].name").value("팔찌"))
            .andExpect(jsonPath("$.subCategories[1].id").value(2))
            .andExpect(jsonPath("$.subCategories[1].name").value("목걸이"));
    }

    @Test
    @DisplayName("전체 카테고리 조회 요청을 처리할 수 있다.")
    void 전체카테고리_조회_요청_처리() throws Exception {
        // given
        UpperProductCategory upperProductCategory1 = UpperProductCategory.builder().id(1L).name("액세서리").build();
        SubProductCategory subProductCategory1 = SubProductCategory.builder().id(1L).name("팔찌").build();
        SubProductCategory subProductCategory2 = SubProductCategory.builder().id(2L).name("목걸이").build();
        upperProductCategory1.addSubProductCategory(subProductCategory1, subProductCategory2);
        UpperProductCategoryItem upperProductCategoryItem1 = UpperProductCategoryItem.from(upperProductCategory1);

        UpperProductCategory upperProductCategory2 = UpperProductCategory.builder().id(2L).name("모자").build();
        SubProductCategory subProductCategory3 = SubProductCategory.builder().id(3L).name("벙거지").build();
        SubProductCategory subProductCategory4 = SubProductCategory.builder().id(4L).name("비니").build();
        upperProductCategory2.addSubProductCategory(subProductCategory3, subProductCategory4);
        UpperProductCategoryItem upperProductCategoryItem2 = UpperProductCategoryItem.from(upperProductCategory2);

        given(upperProductCategoryService.getTotalCategories())
            .willReturn(new UpperProductCategoryItems(List.of(upperProductCategoryItem1, upperProductCategoryItem2)));

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/upper-product-categories")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.totalCategories[0].id").value(1))
            .andExpect(jsonPath("$.totalCategories[0].name").value("액세서리"))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[0].id").value(1))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[0].name").value("팔찌"))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[1].id").value(2))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[1].name").value("목걸이"))
            .andExpect(jsonPath("$.totalCategories[1].id").value(2))
            .andExpect(jsonPath("$.totalCategories[1].name").value("모자"))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[0].id").value(3))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[0].name").value("벙거지"))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[1].id").value(4))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[1].name").value("비니"));
    }

    @Test
    @DisplayName("신규 상위 상품 카테고리 등록 요청을 처리할 수 있다.")
    void 신규_상위카테고리_등록_요청_처리() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"name\" : \"모자\"\n"
            + "}";

        UpperProductCategoryItem responseBody = UpperProductCategoryItem.from(
            UpperProductCategory.builder().id(1L).name("모자").build()
        );

        given(upperProductCategoryService.saveCategory("모자")).willReturn(responseBody);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/upper-product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['id']").value(1))
            .andExpect(jsonPath("$..['name']").value("모자"));
    }

    @Test
    @DisplayName("신규 상위 상품 카테고리 등록 요청 시 데이터가 올바른 형식(Not Blank)인지 검증할 수 있다.")
    void 신규_상위카테고리_등록_요청_데이터_검증() throws Exception {
        // given
        // 빈값, 공백문자, 널값 등 잘못된 형식의 데이터
        String[] requestBodies = {
            "{\n"
            + "    \"name\" : \"\"\n"
            + "}",

            "{\n"
            + "    \"name\" : \"   \"\n"
            + "}",

            "{\n"
            + "    \"name\" : null\n"
            + "}",

            "{\n"
            + "    \"id\" : \n"
            + "}"
        };

        for (String requestBody : requestBodies) {
            // when
            ResultActions resultActions = mockMvc.perform(
                post("/api/upper-product-categories")
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
    @DisplayName("기존 상위 상품 카테고리의 수정 요청을 처리할 수 있다.")
    void 상위카테고리_수정_요청_처리() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"name\" : \"모자\"\n"
            + "}";

        UpperProductCategoryItem responseBody = UpperProductCategoryItem.from(
            UpperProductCategory.builder().id(1L).name("모자").build()
        );

        given(upperProductCategoryService.updateCategory(1L, "모자")).willReturn(responseBody);

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/upper-product-categories/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['id']").value(1))
            .andExpect(jsonPath("$..['name']").value("모자"));
    }

    @Test
    @DisplayName("기존 상위 상품 카테고리의 삭제 요청을 처리할 수 있다.")
    void 상위카테고리_삭제_요청_처리() throws Exception {
        // given
        doNothing().when(upperProductCategoryService).deleteCategory(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/upper-product-categories/1")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        verify(upperProductCategoryService, times(1)).deleteCategory(1L);
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['status']").value("OK"))
            .andExpect(jsonPath("$..['message']").value("정상적으로 삭제 처리되었습니다."));
    }
}
