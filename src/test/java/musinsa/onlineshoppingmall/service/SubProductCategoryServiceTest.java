package musinsa.onlineshoppingmall.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    @DisplayName("새로운 하위 상품 카테고리를 등록 처리할 수 있다.")
    void 하위카테고리_등록() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("신발").build();

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("스니커즈").build();

        given(upperProductCategoryRepository.findAllCategoriesById(1L)).willReturn(Optional.ofNullable(upperProductCategory));
        given(subProductCategoryRepository.save(any(SubProductCategory.class))).willReturn(subProductCategory);

        // when
        SubProductCategoryItem savedCategoryItem = subProductCategoryService.saveCategory(1L, "스니커즈");

        // then
        assertThat(savedCategoryItem.getId()).isEqualTo(1L);
        assertThat(savedCategoryItem.getName()).isEqualTo("스니커즈");
    }

    @Test
    @DisplayName("새로운 하위 상품 카테고리 등록 처리 시 상위 상품 카테고리가 없으면 예외를 발생한다.")
    void 하위카테고리_등록_상위카테고리_비식별_예외() {
        // given
        given(upperProductCategoryRepository.findAllCategoriesById(1L)).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.saveCategory(1L, "스니커즈"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("존재하는 상위 상품 카테고리가 없습니다.");
    }

    @Test
    @DisplayName("새로운 하위 상품 카테고리 등록 처리 시 상위 상품 카테고리와 이름이 중복될 경우 예외를 발생한다.")
    void 하위카테고리_등록_상위카테고리_중복_예외() {
        // given
        UpperProductCategory upperProductCategoryToUpdate = UpperProductCategory.builder()
            .id(1L).name("신발").build();

        given(upperProductCategoryRepository.findAllCategoriesById(1L)).willReturn(Optional.ofNullable(upperProductCategoryToUpdate));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.saveCategory(1L, "신발"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("하위 카테고리는 상위 카테고리와 동일한 이름일 수 없습니다.");
    }

    @Test
    @DisplayName("새로운 하위 상품 카테고리 등록 처리 시 기존 하위 상품 카테고리와 이름이 중복될 경우 예외를 발생한다.")
    void 하위카테고리_등록_하위카테고리_중복_예외() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("신발").build();

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("스니커즈").build();

        upperProductCategory.addSubProductCategory(subProductCategory);

        given(upperProductCategoryRepository.findAllCategoriesById(1L)).willReturn(Optional.ofNullable(upperProductCategory));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.saveCategory(1L, "스니커즈"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 존재하는 하위 카테고리 이름입니다.");
    }

    @Test
    @DisplayName("기존 하위 상품 카테고리의 상위 상품 카테고리와 이름을 수정 처리할 수 있다.")
    void 하위카테고리_수정() {
        // given
        // 수정 대상인 하위 상품 카테고리의 상위 상품 카테고리
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("액세서리").build();

        // 수정 대상인 하위 상품 카테고리
        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).name("구두").upperProductCategory(upperProductCategory).build();

        upperProductCategory.addSubProductCategory(subProductCategory);

        // 수정하고자하는 상위 상품 카테고리
        UpperProductCategory upperProductCategoryToUpdate = UpperProductCategory.builder()
            .id(2L).name("신발").build();

        given(subProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(subProductCategory));
        given(upperProductCategoryRepository.findAllCategoriesById(2L)).willReturn(Optional.ofNullable(upperProductCategoryToUpdate));

        // when
        SubProductCategory updatedSubProductCategory = subProductCategoryService.updateCategory(1L, 2L, "운동화");

        // then
        assertThat(updatedSubProductCategory.getName()).isEqualTo("운동화");
        assertThat(updatedSubProductCategory.getUpperProductCategory()).isEqualTo(upperProductCategoryToUpdate);
    }

    @Test
    @DisplayName("수정하려는 하위 상품 카테고리 이름과 수정하려는 상위 상품 카테고리의 이름이 중복될 경우 예외가 발생한다.")
    void 하위카테고리_수정_상위카테고리_이름_중복_예외() {
        // given
        // 수정 대상인 하위 상품 카테고리의 상위 상품 카테고리
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("액세서리").build();

        // 수정 대상인 하위 상품 카테고리
        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).name("구두").upperProductCategory(upperProductCategory).build();

        upperProductCategory.addSubProductCategory(subProductCategory);

        // 수정하고자하는 상위 상품 카테고리
        UpperProductCategory upperProductCategoryToUpdate = UpperProductCategory.builder()
            .id(2L).name("신발").build();

        given(subProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(subProductCategory));
        given(upperProductCategoryRepository.findAllCategoriesById(2L)).willReturn(Optional.ofNullable(upperProductCategoryToUpdate));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.updateCategory(1L, 2L, "신발"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("하위 카테고리는 상위 카테고리와 동일한 이름일 수 없습니다.");
    }

    @Test
    @DisplayName("수정하려는 하위 상품 카테고리 이름과 수정하려는 상위 상품 카테고리의 속한 하위 카테고리들간 이름이 중복될 경우 예외가 발생한다.")
    void 하위카테고리_수정_하위카테고리_이름_중복_예외() {
        // given
        // 수정 대상인 하위 상품 카테고리의 상위 상품 카테고리
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("액세서리").build();

        // 수정 대상인 하위 상품 카테고리
        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).name("구두").upperProductCategory(upperProductCategory).build();

        upperProductCategory.addSubProductCategory(subProductCategory);

        // 수정하고자하는 상위 상품 카테고리
        UpperProductCategory upperProductCategoryToUpdate = UpperProductCategory.builder()
            .id(2L).name("신발").build();

        // 수정하고자하는 상위 상품 카테고리에 속한 하위 상품 카테고리
        upperProductCategoryToUpdate.addSubProductCategory(SubProductCategory.builder()
            .id(1L).name("운동화").upperProductCategory(upperProductCategory).build());

        given(subProductCategoryRepository.findById(1L)).willReturn(Optional.ofNullable(subProductCategory));
        given(upperProductCategoryRepository.findAllCategoriesById(2L)).willReturn(Optional.ofNullable(upperProductCategoryToUpdate));

        // when, then
        assertThatThrownBy(() -> subProductCategoryService.updateCategory(1L, 2L, "운동화"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 존재하는 하위 카테고리 이름입니다.");
    }

    @Test
    @DisplayName("기존 하위 카테고리를 삭제하는 JpaRepository deleteById 메서드를 호출할 수 있다.")
    void 하위카테고리_삭제_메서드_호출() {
        // given
        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).name("팔찌").build();

        // when
        subProductCategoryService.deleteCategory(1L);

        // then
        verify(subProductCategoryRepository, times(1)).deleteById(1L);
    }
}
