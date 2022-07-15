package musinsa.onlineshoppingmall.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
import musinsa.onlineshoppingmall.repository.UpperProductCategoryRepository;
import musinsa.onlineshoppingmall.repository.SubProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("SubProductCategoryService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class SubProductCategoryServiceTest {

    @InjectMocks
    private SubProductCategoryService subProductCategoryService;

    @Mock
    private SubProductCategoryRepository subProductCategoryRepository;

    @Mock
    private UpperProductCategoryRepository upperProductCategoryRepository;

    @Test
    @DisplayName("새로운 하위 상품 카테고리를 등록할 수 있다.")
    void 하위_상품_카테고리_신규_등록() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L)
            .subProductCategories(new ArrayList<>())
            .name("신발")
            .build();

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L)
            .name("스니커즈")
            .build();

        given(upperProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(upperProductCategory));
        given(subProductCategoryRepository.save(any(SubProductCategory.class))).willReturn(subProductCategory);

        // when
        SubProductCategoryItem savedCategoryItem = subProductCategoryService.saveCategory(1L, "스니커즈");

        // then
        assertThat(savedCategoryItem.getId()).isEqualTo(1L);
        assertThat(savedCategoryItem.getName()).isEqualTo("스니커즈");
    }

    @Test
    @DisplayName("새로운 하위 상품 카테고리 등록 시 상위 상품 카테고리가 없으면 예외를 발생한다.")
    void 상위_카테고리_비식별_예외() {
        // given
        given(upperProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.saveCategory(1L, "스니커즈"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("존재하는 상위 상품 카테고리가 없습니다.");
    }

    @Test
    @DisplayName("새로운 하위 상품 카테고리 등록 시 상위 카테고리와 이름이 중복될 경우 예외를 발생한다.")
    void 상위_카테고리명_중복_예외() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L)
            .name("신발")
            .build();

        given(upperProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(upperProductCategory));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.saveCategory(1L, "신발"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("하위 카테고리는 상위 카테고리와 동일한 이름일 수 없습니다.");
    }

    @Test
    @DisplayName("새로운 하위 상품 카테고리 등록 시 기존 하위 카테고리와 이름이 중복될 경우 예외를 발생한다.")
    void 하위_카테고리명_중복_예외() {
        // given
        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L)
            .name("스니커즈")
            .build();

        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L)
            .subProductCategories(List.of(subProductCategory))
            .name("신발")
            .build();

        given(upperProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(upperProductCategory));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.saveCategory(1L, "스니커즈"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 존재하는 하위 카테고리 이름입니다.");
    }
}
