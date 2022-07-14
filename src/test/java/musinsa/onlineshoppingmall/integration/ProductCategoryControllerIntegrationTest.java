package musinsa.onlineshoppingmall.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import musinsa.onlineshoppingmall.service.ProductCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("ProductCategoryController 통합테스트")
@SpringBootTest
@AutoConfigureMockMvc
public class ProductCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Test
    @DisplayName("신규 상품 카테고리를 등록할 수 있다.")
    void 신규_상품_카테고리_등록() throws Exception {
        // given
        String requestBody = "{\n"
            + "    \"name\" : \"신발\"\n"
            + "}";

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

}
