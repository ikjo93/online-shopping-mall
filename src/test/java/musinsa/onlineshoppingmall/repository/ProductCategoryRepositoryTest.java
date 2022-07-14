package musinsa.onlineshoppingmall.repository;

import static org.assertj.core.api.Assertions.assertThat;

import musinsa.onlineshoppingmall.domain.ProductCategory;
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

    @Test
    @DisplayName("상품 카테고리 이름에 해당되는 상품 카테고리 정보를 조회할 수 있다.")
    void 이름별_상품_카테고리_정보_조회() {
        // given
        ProductCategory savedProductCategory = productCategoryRepository.save(
            ProductCategory.builder()
                .name("신발")
                .build()
        );

        // when
        ProductCategory findProductCategory = productCategoryRepository.findByName("신발");

        // then
        assertThat(findProductCategory).isEqualTo(savedProductCategory);
    }
}
