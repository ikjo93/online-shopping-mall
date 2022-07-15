package musinsa.onlineshoppingmall.repository;

import java.util.List;
import java.util.Optional;
import musinsa.onlineshoppingmall.domain.UpperProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UpperProductCategoryRepository extends JpaRepository<UpperProductCategory, Long> {

    @Query("select distinct u from UpperProductCategory u left join fetch u.subProductCategories where u.id = :id")
    Optional<UpperProductCategory> findAllCategoriesById(@Param("id") Long id);

    @Query("select distinct u from UpperProductCategory u left join fetch u.subProductCategories")
    List<UpperProductCategory> findAllCategories();

    UpperProductCategory findByName(String name);

}
