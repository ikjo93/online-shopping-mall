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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private Long id;

    @OneToMany(mappedBy = "productCategory")
    private List<SubProductCategory> subItemCategories = new ArrayList<>();

    private String name;

    private ProductCategory(String name) {
        this.name = name;
    }

    public static ProductCategory of(String name) {
        return new ProductCategory(name);
    }

}
