package musinsa.onlineshoppingmall.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.repository.UpperProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("UpperProductCategoryController 통합테스트")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UpperProductCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UpperProductCategoryRepository upperProductCategoryRepository;

    @Test
    @DisplayName("상위 상품 카테고리 식별자로 해당 하위 상품 카테고리들을 조회할 수 있다.")
    void 상위카테고리_식별자로_하위카테고리_조회() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/upper-product-categories/1")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.subCategories[0].id").value(1))
            .andExpect(jsonPath("$.subCategories[0].name").value("브이넥 티셔츠"))
            .andExpect(jsonPath("$.subCategories[1].id").value(2))
            .andExpect(jsonPath("$.subCategories[1].name").value("라운드 티셔츠"));
    }

    @Test
    @DisplayName("상위 상품 카테고리를 지정하지 않았을 때 미분류 하위 카테고리를 포함한 전체 상품 카테고리를 조회할 수 있다.")
    void 전체카테고리_조회() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/upper-product-categories")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.totalCategories[0].id").value(1))
            .andExpect(jsonPath("$.totalCategories[0].name").value("티셔츠"))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[0].id").value(1))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[0].name").value("브이넥 티셔츠"))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[1].id").value(2))
            .andExpect(jsonPath("$.totalCategories[0].subCategories[1].name").value("라운드 티셔츠"))
            .andExpect(jsonPath("$.totalCategories[1].id").value(2))
            .andExpect(jsonPath("$.totalCategories[1].name").value("바지"))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[0].id").value(3))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[0].name").value("반바지"))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[1].id").value(4))
            .andExpect(jsonPath("$.totalCategories[1].subCategories[1].name").value("냉장고 바지"))
            .andExpect(jsonPath("$.totalCategories[2].id").value(3))
            .andExpect(jsonPath("$.totalCategories[2].name").value("신발"))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[0].id").value(5))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[0].name").value("운동화"))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[1].id").value(6))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[1].name").value("스니커즈"))
            .andExpect(jsonPath("$.totalCategories[3].id").doesNotExist())
            .andExpect(jsonPath("$.totalCategories[3].name").value("UNCLASSIFIED"))
            .andExpect(jsonPath("$.totalCategories[3].subCategories").isEmpty());
    }

    @Test
    @DisplayName("기존에 존재하는 상위 상품 카테고리와 이름이 중복되지 않았을 때 신규 상위 상품 카테고리를 등록할 수 있다.")
    void 신규_상위카테고리_등록() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"name\" : \"모자\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/upper-product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        Optional<UpperProductCategory> result = upperProductCategoryRepository.findById(4L);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo("모자");

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['id']").value(4))
            .andExpect(jsonPath("$..['name']").value("모자"));
    }

    @Test
    @DisplayName("신규 상위 상품 카테고리를 등록 시 기존에 존재하는 상위 상품 카테고리와 이름이 중복될 때 예외가 발생한다.")
    void 신규_상위카테고리_등록_예외() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"name\" : \"티셔츠\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/upper-product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isConflict())
            .andExpect(jsonPath("$..['status']").value("CONFLICT"))
            .andExpect(jsonPath("$..['message']").value("이미 존재하는 상위 상품 카테고리 이름입니다."));
    }

    @Test
    @DisplayName("기존 상위 상품 카테고리의 이름을 수정할 수 있다.")
    void 상위카테고리_이름_수정() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"name\" : \"상의\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/upper-product-categories/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        Optional<UpperProductCategory> result = upperProductCategoryRepository.findById(1L);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo("상의");

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("상의"));
    }

    @Test
    @DisplayName("기존 상위 상품 카테고리의 이름 수정 시 다른 상위 상품 카테고리와 이름이 중복될 경우 예외를 발생한다.")
    void 상위카테고리_이름_수정_상위중복_예외() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"name\" : \"바지\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/upper-product-categories/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isConflict())
            .andExpect(jsonPath("$..['status']").value("CONFLICT"))
            .andExpect(jsonPath("$..['message']").value("이미 존재하는 상위 상품 카테고리 이름입니다."));
    }

    @Test
    @DisplayName("기존 상위 상품 카테고리의 이름 수정 시 해당 상위 상품 카테고리의 하위 카테고리와 이름이 중복될 경우 예외를 발생한다.")
    void 상위카테고리_이름_수정_하위중복_예외() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"name\" : \"브이넥 티셔츠\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/upper-product-categories/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isConflict())
            .andExpect(jsonPath("$..['status']").value("CONFLICT"))
            .andExpect(jsonPath("$..['message']").value("이미 존재하는 하위 카테고리 이름입니다."));
    }

    @Test
    @DisplayName("기존 상위 상품 카테고리를 삭제할 수 있고 해당 하위 상품 카테고리는 미분류 상태로 처리된다.")
    void 상위카테고리_삭제() throws Exception {
        // given

        // when
        ResultActions resultAction1 = mockMvc.perform(
            delete("/api/upper-product-categories/1")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        assertThat(upperProductCategoryRepository.findById(1L).isEmpty()).isTrue();

        resultAction1.andExpect(status().isOk())
            .andExpect(jsonPath("$..['status']").value("OK"))
            .andExpect(jsonPath("$..['message']").value("정상적으로 삭제 처리되었습니다."));

        // when
        ResultActions resultAction2 = mockMvc.perform(
            get("/api/upper-product-categories")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultAction2.andExpect(status().isOk())
            .andExpect(jsonPath("$.totalCategories[2].id").doesNotExist())
            .andExpect(jsonPath("$.totalCategories[2].name").value("UNCLASSIFIED"))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[0].id").value(1))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[0].name").value("브이넥 티셔츠"))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[1].id").value(2))
            .andExpect(jsonPath("$.totalCategories[2].subCategories[1].name").value("라운드 티셔츠"));
    }
}
