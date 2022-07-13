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
@Table(name = "brand_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_category_id")
    private Long id;

    @OneToMany(mappedBy = "brandCategory")
    private List<ItemCategory> itemCategories = new ArrayList<>();

    private String name;

}
