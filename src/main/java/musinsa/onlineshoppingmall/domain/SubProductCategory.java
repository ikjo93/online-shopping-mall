package musinsa.onlineshoppingmall.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_product_category")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubProductCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_product_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_product_category_id")
    @NotNull
    private UpperProductCategory upperProductCategory;

    private String name;

    /**
     * 비지니스 로직
     */
    public boolean hasSameName(String name) {
        return this.name.equals(name);
    }

    public void updateInfo(UpperProductCategory upperProductCategory, String name) {
        this.upperProductCategory.getSubProductCategories().remove(this);
        this.upperProductCategory = upperProductCategory;
        this.upperProductCategory.addSubProductCategory(this);
        this.name = name;
    }

    public void initUpperProductCategory() {
        this.upperProductCategory = null;
    }
}
