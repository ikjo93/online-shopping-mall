package musinsa.onlineshoppingmall.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import musinsa.onlineshoppingmall.domain.ProductCategory;
import musinsa.onlineshoppingmall.dto.ProductCategoryCreateForm;
import musinsa.onlineshoppingmall.dto.ProductCategoryItem;
import musinsa.onlineshoppingmall.repository.ProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductCategoryService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Test
    @DisplayName("새로운 상품 카테고리를 등록할 수 있다.")
    void 상품_카테고리_신규_등록() {
        // given
        given(productCategoryRepository.findByName("신발")).willReturn(null);
        given(productCategoryRepository.save(any(ProductCategory.class))).willReturn(
            ProductCategory.builder()
                .id(1l)
                .name("신발")
                .build()
        );

        // when
        ProductCategoryItem shoesCategory = productCategoryService.saveCategory(
            new ProductCategoryCreateForm("신발")
        );

        // then
        assertThat(shoesCategory.getId()).isEqualTo(1l);
        assertThat(shoesCategory.getName()).isEqualTo("신발");
    }

    @Test
    @DisplayName("기존에 존재하는 이름으로 상품 카테고리를 등록 시 예외가 발생한다.")
    void 상품_카테고리_중복_등록_예외() {
        // given
        given(productCategoryRepository.findByName("신발")).willReturn(
            ProductCategory.builder()
                .id(1l)
                .name("신발")
                .build()
        );

        // when, then
        assertThatThrownBy(() -> productCategoryService.saveCategory(
            new ProductCategoryCreateForm("신발")
        ))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 존재하는 카테고리입니다.");
    }
}
