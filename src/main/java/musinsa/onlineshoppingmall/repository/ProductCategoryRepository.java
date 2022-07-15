package musinsa.onlineshoppingmall.repository;

import java.util.List;
import java.util.Optional;
import musinsa.onlineshoppingmall.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Override
    @Query("select distinct pc from ProductCategory pc left join fetch pc.subItemCategories where pc.id = :id")
    Optional<ProductCategory> findById(@Param("id") Long id);

    @Override
    @Query("select distinct pc from ProductCategory pc left join fetch pc.subItemCategories")
    List<ProductCategory> findAll();

    ProductCategory findByName(String name);

}
