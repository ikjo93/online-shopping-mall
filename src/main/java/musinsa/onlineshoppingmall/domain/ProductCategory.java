package musinsa.onlineshoppingmall.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_category")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private Long id;

    @OneToMany(mappedBy = "productCategory")
    private List<SubProductCategory> subProductCategories = new ArrayList<>();

    private String name;

    public void validateDuplicateName(String name) {
        if (this.name.equals(name)) {
            throw new IllegalStateException("상위 카테고리와 동일한 이름일 수 없습니다.");
        }

        validateNameOfSubProductCategories(name);
    }

    private void validateNameOfSubProductCategories(String name) {
        subProductCategories.stream()
            .filter(subProductCategory -> subProductCategory.hasSameName(name))
            .findFirst().ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 하위 카테고리입니다.");
            });
    }
}
