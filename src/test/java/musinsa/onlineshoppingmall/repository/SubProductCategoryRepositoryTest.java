package musinsa.onlineshoppingmall.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("SubProductCategoryRepository 단위 테스트")
@DataJpaTest
@AutoConfigureTestEntityManager
public class SubProductCategoryRepositoryTest {

    @Autowired
    private SubProductCategoryRepository subProductCategoryRepository;

    @Autowired
    private UpperProductCategoryRepository upperProductCategoryRepository;

    @Test
    @DisplayName("상위 카테고리가 등록되지 않은 하위 카테고리들을 조회할 수 있다.")
    void 상위카테고리_미등록_하위카테고리_조회() {
        // given
        UpperProductCategory upperProductCategory = UpperProductCategory.builder().name("모자").build();

        SubProductCategory subProductCategory1 = SubProductCategory.builder().name("벙거지").build();
        SubProductCategory subProductCategory2 = SubProductCategory.builder().name("스냅백").build();
        SubProductCategory subProductCategory3 = SubProductCategory.builder().name("캡").build();
        SubProductCategory subProductCategory4 = SubProductCategory.builder().upperProductCategory(upperProductCategory).name("베레모").build();

        upperProductCategoryRepository.save(upperProductCategory);
        subProductCategoryRepository.save(subProductCategory1);
        subProductCategoryRepository.save(subProductCategory2);
        subProductCategoryRepository.save(subProductCategory3);
        subProductCategoryRepository.save(subProductCategory4);
        subProductCategoryRepository.flush();

        // when
        List<SubProductCategory> unclassifiedSubProductCategories = subProductCategoryRepository.findAllByUpperProductCategoryIsNull();

        // then
        assertThat(unclassifiedSubProductCategories.size()).isEqualTo(3);
    }

}
