package musinsa.onlineshoppingmall.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SubProductCategory 도메인 단위 테스트")
public class SubProductCategoryTest {

    @Test
    @DisplayName("하위 상품 카테고리의 상위 상품 카테고리와 이름을 수정할 수 있다.")
    void 하위카테고리_정보_수정() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("베레모").build();

        upperProductCategory.addSubProductCategory(subProductCategory);

        UpperProductCategory upperProductCategoryToUpdate = UpperProductCategory.builder()
            .id(1L).name("바지").build();

        // when
        subProductCategory.update(upperProductCategoryToUpdate, "청바지");

        // then
        assertThat(subProductCategory.getUpperProductCategory()).isEqualTo(upperProductCategoryToUpdate);
        assertThat(subProductCategory.getName()).isEqualTo("청바지");
    }

    @Test
    @DisplayName("상위 상품 카테고리에 대한 하위 상품 카테고리의 참조 관계를 제거할 수 있다.")
    void 상위카테고리_연관관계_제거() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder()
            .id(1L).name("모자").build();

        SubProductCategory subProductCategory = SubProductCategory.builder()
            .id(1L).upperProductCategory(upperProductCategory).name("베레모").build();

        // when
        UpperProductCategory result = subProductCategory.getUpperProductCategory();

        // given
        assertThat(result).isEqualTo(upperProductCategory);

        // when
        subProductCategory.initUpperProductCategory();

        // then
        assertThat(subProductCategory.getUpperProductCategory()).isNull();
    }
}
