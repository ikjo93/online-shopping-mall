package musinsa.onlineshoppingmall.repository;

import java.util.List;
import java.util.Optional;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("select distinct pc from ProductCategory pc left join fetch pc.subItemCategories where pc.id = :id")
    Optional<ProductCategory> findAllCategoriesById(@Param("id") Long id);

    @Query("select distinct pc from ProductCategory pc left join fetch pc.subItemCategories")
    List<ProductCategory> findAllCategories();

    ProductCategory findByName(String name);

}
