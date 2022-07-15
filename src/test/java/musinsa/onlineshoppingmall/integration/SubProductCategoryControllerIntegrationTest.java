package musinsa.onlineshoppingmall.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import musinsa.onlineshoppingmall.dto.ProductCategoryItem;
import musinsa.onlineshoppingmall.service.ProductCategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("SubProductCategoryControllerIntegrationTest 통합테스트")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SubProductCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    @DisplayName("신규 하위 상품 카테고리를 등록할 수 있다.")
    void 신규_하위_상품_카테고리_등록() throws Exception {
        // given
        ProductCategoryItem productCategoryItem = productCategoryService.saveCategory("신발");

        String requestBody = "{\n"
            + "    \"parentCategoryId\" : " + productCategoryItem.getId() + ",\n"
            + "    \"name\" : \"스니커즈\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/sub-product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['name']").value("스니커즈"));
    }
}
