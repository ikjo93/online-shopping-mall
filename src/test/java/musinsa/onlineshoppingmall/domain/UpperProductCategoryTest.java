package musinsa.onlineshoppingmall.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import musinsa.onlineshoppingmall.dto.subproductcategory.SubProductCategoryItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UpperProductCategory 도메인 단위 테스트")
public class UpperProductCategoryTest {

    @Test
    @DisplayName("상위 상품 카테고리에 속한 하위 상품 카테고리 아이템 리스트를 반환할 수 있다.")
    void 하위카테고리_반환() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();
        SubProductCategory subProductCategory1 = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("스냅백").build();
        SubProductCategory subProductCategory2 = SubProductCategory.builder()
            .id(2L).upperProductCategory(upperProductCategory).name("베레모").build();

        upperProductCategory.addSubProductCategory(subProductCategory1, subProductCategory2);

        // when
        List<SubProductCategoryItem> subProductCategoryItems = upperProductCategory.subProductCategoryItems();

        // then
        assertThat(subProductCategoryItems.get(0).getId()).isEqualTo(1L);
        assertThat(subProductCategoryItems.get(0).getName()).isEqualTo("스냅백");
        assertThat(subProductCategoryItems.get(1).getId()).isEqualTo(2L);
        assertThat(subProductCategoryItems.get(1).getName()).isEqualTo("베레모");
    }

    @Test
    @DisplayName("새로 추가할 하위 상품 카테고리가 상위 상품 카테고리와 이름이 같으면 예외를 발생한다.")
    void 하위카테고리_상위카테고리_중복이름_예외() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("모자").build();

        // when, then
        assertThatThrownBy(() -> upperProductCategory.addSubProductCategory(subProductCategory))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("하위 카테고리는 상위 카테고리와 동일한 이름일 수 없습니다.");
    }

    @Test
    @DisplayName("새로 추가할 하위 상품 카테고리가 상위 상품 카테고리에 속한 하위 상품 카테고리들 간 이름이 같으면 예외를 발생한다.")
    void 하위카테고리_하위카테고리_중복이름_예외() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();

        SubProductCategory subProductCategory1 = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("베레모").build();

        upperProductCategory.addSubProductCategory(subProductCategory1);

        SubProductCategory subProductCategory2 = SubProductCategory.builder()
            .id(2L).upperProductCategory(upperProductCategory).name("베레모").build();

        // when, then
        assertThatThrownBy(() -> upperProductCategory.addSubProductCategory(subProductCategory2))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 존재하는 하위 카테고리 이름입니다.");
    }

    @Test
    @DisplayName("상위 상품 카테고리의 이름을 수정할 수 있다.")
    void 상위카테고리_이름_수정() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();

        // when
        upperProductCategory.updateName("바지");

        // then
        assertThat(upperProductCategory.getName()).isEqualTo("바지");
    }

    @Test
    @DisplayName("상위 상품 카테고리의 이름을 수정 시 속한 하위 상품 카테고리의 이름과 중복되면 예외를 발생한다.")
    void 상위카테고리_이름_수정_중복_예외() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();

        SubProductCategory subProductCategory1 = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("베레모").build();

        upperProductCategory.addSubProductCategory(subProductCategory1);

        // when, then
        assertThatThrownBy(() -> upperProductCategory.updateName("베레모"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 존재하는 하위 카테고리 이름입니다.");
    }

    @Test
    @DisplayName("상위 상품 카테고리에 속한 특정 하위 상품 카테고리를 제거할 수 있다.")
    void 하위카테고리_삭제() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();

        SubProductCategory subProductCategory1 = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("베레모").build();

        // when
        upperProductCategory.addSubProductCategory(subProductCategory1);

        // then
        assertThat(upperProductCategory.subProductCategoryItems().size()).isEqualTo(1);

        // when
        upperProductCategory.removeSubProductCategory(subProductCategory1);

        // then
        assertThat(upperProductCategory.subProductCategoryItems().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("상위 상품 카테고리에 속한 하위 상품 카테고리와의 연관 관계를 제거할 수 있다.")
    void 하위카테고리_연관관계_제거() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();
        SubProductCategory subProductCategory1 = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("스냅백").build();
        SubProductCategory subProductCategory2 = SubProductCategory.builder()
            .id(2L).upperProductCategory(upperProductCategory).name("베레모").build();

        // when
        upperProductCategory.addSubProductCategory(subProductCategory1, subProductCategory2);

        // then
        List<SubProductCategory> result1 = upperProductCategory.getSubProductCategories();
        assertThat(result1.get(0).getUpperProductCategory()).isEqualTo(upperProductCategory);
        assertThat(result1.get(1).getUpperProductCategory()).isEqualTo(upperProductCategory);

        // when
        upperProductCategory.removeSubProductCategories();

        // then
        List<SubProductCategory> result2 = upperProductCategory.getSubProductCategories();
        assertThat(result2.get(0).getUpperProductCategory()).isNull();
        assertThat(result2.get(1).getUpperProductCategory()).isNull();
    }
}
