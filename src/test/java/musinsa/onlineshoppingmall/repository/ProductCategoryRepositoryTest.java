package musinsa.onlineshoppingmall.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("ProductCategoryRepository 단위 테스트")
@DataJpaTest
@AutoConfigureTestEntityManager
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    private ProductCategory savedProductCategory;

    @BeforeEach
    void setup() {
        ProductCategory productCategory = ProductCategory.builder()
            .name("신발")
            .build();

        savedProductCategory = productCategoryRepository.save(productCategory);
    }

    @Test
    @DisplayName("상품 카테고리 식별자로 상품 카테고리 정보를 조회할 수 있다.")
    void 식별자별_상품_카테고리_정보_조회() {
        // given
        Long id = savedProductCategory.getId();

        // when
        Optional<ProductCategory> findProductCategory = productCategoryRepository.findAllCategoriesById(id);

        // then
        assertThat(findProductCategory.isPresent()).isTrue();
        assertThat(findProductCategory.get().getName()).isEqualTo("신발");
    }

    @Test
    @DisplayName("전체 상품 카테고리 정보를 조회할 수 있다.")
    void 전체_상품_카테고리_정보_조회() {
        // given
        ProductCategory topsCategory = ProductCategory.builder()
            .name("상의")
            .build();

        ProductCategory bottomsCategory = ProductCategory.builder()
            .name("하의")
            .build();

        ProductCategory hatCategory = ProductCategory.builder()
            .name("모자")
            .build();

        productCategoryRepository.save(topsCategory);
        productCategoryRepository.save(bottomsCategory);
        productCategoryRepository.save(hatCategory);

        // when
        List<ProductCategory> categories = productCategoryRepository.findAllCategories();

        // then
        assertThat(categories.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("상품 카테고리 이름으로 상품 카테고리 정보를 조회할 수 있다.")
    void 이름별_상품_카테고리_정보_조회() {
        // given
        String name = savedProductCategory.getName();

        // when
        ProductCategory findProductCategory = productCategoryRepository.findByName(name);

        // then
        assertThat(findProductCategory).isEqualTo(savedProductCategory);
    }
}
