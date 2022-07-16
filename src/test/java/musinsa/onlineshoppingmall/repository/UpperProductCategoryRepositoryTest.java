package musinsa.onlineshoppingmall.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("UpperProductCategoryRepository 단위 테스트")
@DataJpaTest
@AutoConfigureTestEntityManager
class UpperProductCategoryRepositoryTest {

    @Autowired
    private UpperProductCategoryRepository upperProductCategoryRepository;

    private UpperProductCategory savedUpperProductCategory;

    @BeforeEach
    void setup() {
        UpperProductCategory upperProductCategory = UpperProductCategory.builder().name("모자").build();
        savedUpperProductCategory = upperProductCategoryRepository.save(upperProductCategory);
        upperProductCategoryRepository.flush();
    }

    @Test
    @DisplayName("상품 카테고리 식별자로 상품 카테고리 정보를 조회할 수 있다.")
    void 식별자별_상품_카테고리_정보_조회() {
        // given
        Long id = savedUpperProductCategory.getId();

        // when
        Optional<UpperProductCategory> findProductCategory = upperProductCategoryRepository.findAllCategoriesById(id);

        // then
        assertThat(findProductCategory.isPresent()).isTrue();
        assertThat(findProductCategory.get().getName()).isEqualTo("모자");
    }

    @Test
    @DisplayName("전체 상품 카테고리 정보를 조회할 수 있다.")
    void 전체_상품_카테고리_정보_조회() {
        // given
        UpperProductCategory topsCategory = UpperProductCategory.builder().name("상의").build();
        UpperProductCategory bottomsCategory = UpperProductCategory.builder().name("하의").build();
        UpperProductCategory hatCategory = UpperProductCategory.builder().name("액세서리").build();

        upperProductCategoryRepository.save(topsCategory);
        upperProductCategoryRepository.save(bottomsCategory);
        upperProductCategoryRepository.save(hatCategory);
        upperProductCategoryRepository.flush();

        // when
        List<UpperProductCategory> categories = upperProductCategoryRepository.findAllCategories();

        // then
        assertThat(categories.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("상품 카테고리 이름으로 상품 카테고리 정보를 조회할 수 있다.")
    void 이름별_상품_카테고리_정보_조회() {
        // given
        String name = savedUpperProductCategory.getName();

        // when
        UpperProductCategory findUpperProductCategory = upperProductCategoryRepository.findByName(name);

        // then
        assertThat(findUpperProductCategory).isEqualTo(savedUpperProductCategory);
    }
}
