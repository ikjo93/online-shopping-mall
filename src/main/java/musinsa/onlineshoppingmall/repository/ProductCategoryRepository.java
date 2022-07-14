package musinsa.onlineshoppingmall.repository;

import musinsa.onlineshoppingmall.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    ProductCategory findByName(String name);

}
