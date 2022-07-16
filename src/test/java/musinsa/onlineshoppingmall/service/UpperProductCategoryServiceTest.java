package musinsa.onlineshoppingmall.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItems;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItem;
import musinsa.onlineshoppingmall.dto.upperproductcategory.UpperProductCategoryItems;
import musinsa.onlineshoppingmall.repository.SubProductCategoryRepository;
import musinsa.onlineshoppingmall.repository.UpperProductCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UpperProductCategoryService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class UpperProductCategoryServiceTest {

    @InjectMocks
    private UpperProductCategoryService upperProductCategoryService;

    @Mock
    private UpperProductCategoryRepository upperProductCategoryRepository;

    @Mock
    private SubProductCategoryRepository subProductCategoryRepository;

    @Test
    @DisplayName("상위 상품 카테고리 식별자로 해당 하위 상품 카테고리들을 조회할 수 있다.")
    void 상위카테고리_속한_하위카테고리_조회() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L)
            .name("모자")
            .build();

        SubProductCategory subProductCategory1 = SubProductCategory.builder()
            .id(1L)
            .name("벙거지")
            .upperProductCategory(upperProductCategory)
            .build();

        SubProductCategory subProductCategory2 = SubProductCategory.builder()
            .id(2L)
            .name("스냅백")
            .upperProductCategory(upperProductCategory)
            .build();

        SubProductCategory subProductCategory3 = SubProductCategory.builder()
            .id(3L)
            .name("캡")
            .upperProductCategory(upperProductCategory)
            .build();

        upperProductCategory.getSubProductCategories().addAll(List.of(subProductCategory1, subProductCategory2, subProductCategory3));

        given(upperProductCategoryRepository.findAllCategoriesById(1L)).willReturn(Optional.ofNullable(upperProductCategory));

        // when
        SubProductCategoryItems subProductCategoryItems = upperProductCategoryService.getSubCategoriesById(1L);

        // then
        assertThat(subProductCategoryItems.getSubCategories().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("식별자(id)로 상위 상품 카테고리가 조회되지 않을 때 예외가 발생한다.")
    void 비식별_상위카테고리_조회시_예외() {
        // given
        given(upperProductCategoryRepository.findAllCategoriesById(1L)).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> upperProductCategoryService.getSubCategoriesById(1L))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("존재하는 상품 카테고리가 없습니다.");
    }

    @Test
    @DisplayName("전체 카테고리를 조회할 수 있다.")
    void 전체_카테고리_조회() {
        // given
        UpperProductCategory upperProductCategory1 = UpperProductCategory.builder()
            .id(1L)
            .name("모자")
            .build();

        UpperProductCategory upperProductCategory2 = UpperProductCategory.builder()
            .id(2L)
            .name("액세서리")
            .build();

        SubProductCategory subProductCategory1 = SubProductCategory.builder()
            .id(1L)
            .name("벙거지")
            .upperProductCategory(upperProductCategory1)
            .build();

        SubProductCategory subProductCategory2 = SubProductCategory.builder()
            .id(2L)
            .name("스냅백")
            .upperProductCategory(upperProductCategory1)
            .build();

        SubProductCategory subProductCategory3 = SubProductCategory.builder()
            .id(3L)
            .name("목걸이")
            .upperProductCategory(upperProductCategory2)
            .build();

        SubProductCategory subProductCategory4 = SubProductCategory.builder()
            .id(4L)
            .name("와이드 팬츠")
            .build();

        upperProductCategory1.getSubProductCategories().addAll(List.of(subProductCategory1, subProductCategory2));
        upperProductCategory2.getSubProductCategories().addAll(List.of(subProductCategory3));

        given(upperProductCategoryRepository.findAllCategories()).willReturn(List.of(upperProductCategory1, upperProductCategory2));
        given(subProductCategoryRepository.findAllByUpperProductCategoryIsNull()).willReturn(List.of(subProductCategory4));

        // when
        UpperProductCategoryItems totalCategories = upperProductCategoryService.getTotalCategories();

        // then
        List<UpperProductCategoryItem> result = totalCategories.getTotalCategories();
        assertThat(result.size()).isEqualTo(3); // 실제 상위 카테고리는 2개이나, 미분류된 하위 카테고리를 포함하는 상위 카테고리를 포함하여 3개
        assertThat(result.get(0).getSubCategories().size()).isEqualTo(2);
        assertThat(result.get(1).getSubCategories().size()).isEqualTo(1);
        assertThat(result.get(2).getSubCategories().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("새로운 상위 상품 카테고리를 등록할 수 있다.")
    void 상위카테고리_신규_등록() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L)
            .name("신발")
            .build();

        given(upperProductCategoryRepository.findByName("신발")).willReturn(null);
        given(upperProductCategoryRepository.save(any(UpperProductCategory.class))).willReturn(
            upperProductCategory);

        // when
        UpperProductCategoryItem shoesCategory = upperProductCategoryService.saveCategory("신발");

        // then
        assertThat(shoesCategory.getId()).isEqualTo(1l);
        assertThat(shoesCategory.getName()).isEqualTo("신발");
    }

    @Test
    @DisplayName("기존에 존재하는 이름으로 상위 상품 카테고리를 등록 시 예외가 발생한다.")
    void 상위카테고리_중복_등록_예외() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L)
            .name("신발")
            .build();

        given(upperProductCategoryRepository.findByName("신발")).willReturn(upperProductCategory);

        // when, then
        assertThatThrownBy(() -> upperProductCategoryService.saveCategory("신발"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 존재하는 카테고리입니다.");
    }

    @Test
    @DisplayName("기존에 존재하는 상위 카테고리 이름을 수정할 수 있다.")
    void 상위카테고리_이름_수정() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L)
            .name("모자")
            .build();

        given(upperProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(upperProductCategory));

        // when
        UpperProductCategoryItem upperProductCategoryItem = upperProductCategoryService.updateCategory(1L, "액세서리");

        // then
        assertThat(upperProductCategoryItem.getName()).isEqualTo("액세서리");
    }

    @Test
    @DisplayName("기존에 존재하는 상위 카테고리를 삭제하는 JpaRepository deleteById 메서드를 호출할 수 있다.")
    void 상위카테고리_삭제() {
        // when
        upperProductCategoryService.deleteCategory(1L);

        // then
        verify(upperProductCategoryRepository, times(1)).deleteById(1L);
    }
}
