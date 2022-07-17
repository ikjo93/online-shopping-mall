package musinsa.onlineshoppingmall.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.repository.SubProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("SubProductCategoryController 통합테스트")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SubProductCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubProductCategoryRepository subProductCategoryRepository;

    @Test
    @DisplayName("신규 하위 상품 카테고리를 등록할 수 있다.")
    void 신규_하위카테고리_등록() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"오버핏 티셔츠\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/sub-product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        Optional<SubProductCategory> result = subProductCategoryRepository.findById(7L);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo("오버핏 티셔츠");

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['id']").value(7))
            .andExpect(jsonPath("$..['name']").value("오버핏 티셔츠"));
    }

    @Test
    @DisplayName("신규 하위 상품 카테고리 등록 시 해당 상위 상품 카테고리와 이름이 동일할 경우 예외가 발생한다.")
    void 신규_하위카테고리_등록_상위중복_예외() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"티셔츠\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/sub-product-categories")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isConflict())
            .andExpect(jsonPath("$..['status']").value("CONFLICT"))
            .andExpect(jsonPath("$..['message']").value("하위 카테고리는 상위 카테고리와 동일한 이름일 수 없습니다."));
    }

    @Test
    @DisplayName("신규 하위 상품 카테고리 등록 시 같은 하위 상품 카테고리 간 이름이 동일할 경우 예외가 발생한다.")
    void 신규_하위카테고리_등록_하위중복_예외() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"라운드 티셔츠\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/sub-product-categories")
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
    @DisplayName("기존 하위 상품 카테고리의 상위 카테고리와 이름을 수정할 수 있다.")
    void 하위카테고리_수정() throws Exception {
        // given
        String requestBody =
            "{\n"
                + "    \"upperProductCategoryId\" : 1,\n"
                + "    \"name\" : \"오버핏 티셔츠\"\n"
                + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/sub-product-categories/3")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        Optional<SubProductCategory> result = subProductCategoryRepository.findById(3L);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo("오버핏 티셔츠");
        assertThat(result.get().getUpperProductCategory().getName()).isEqualTo("티셔츠");

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['id']").value(3))
            .andExpect(jsonPath("$..['name']").value("오버핏 티셔츠"));
    }

    @Test
    @DisplayName("기존 하위 상품 카테고리의 변경하려는 이름과 변경하려는 상위 상품 카테고리의 이름이 중복될 경우 예외를 발생한다.")
    void 하위카테고리_수정_상위중복_예외() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"티셔츠\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/sub-product-categories/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isConflict())
            .andExpect(jsonPath("$..['status']").value("CONFLICT"))
            .andExpect(jsonPath("$..['message']").value("하위 카테고리는 상위 카테고리와 동일한 이름일 수 없습니다."));
    }

    @Test
    @DisplayName("기존 하위 상품 카테고리의 변경하려는 이름과 변경하려는 상위 상품 카테고리의 속한 하위 상품 카테고리들 간 이름이 중복될 경우 예외를 발생한다.")
    void 하위카테고리_수정_하위중복_예외() throws Exception {
        // given
        String requestBody =
            "{\n"
            + "    \"upperProductCategoryId\" : 1,\n"
            + "    \"name\" : \"라운드 티셔츠\"\n"
            + "}";

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/sub-product-categories/4")
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
    @DisplayName("기존 하위 상품 카테고리를 삭제할 수 있다.")
    void 하위카테고리_삭제() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/sub-product-categories/3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        assertThat(subProductCategoryRepository.findById(3L).isEmpty()).isTrue();

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$..['status']").value("OK"))
            .andExpect(jsonPath("$..['message']").value("정상적으로 삭제 처리되었습니다."));
    }
}
