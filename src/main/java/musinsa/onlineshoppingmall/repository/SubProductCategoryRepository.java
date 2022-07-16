package musinsa.onlineshoppingmall.repository;

import java.util.List;
import musinsa.onlineshoppingmall.domain.SubProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubProductCategoryRepository extends JpaRepository<SubProductCategory, Long> {

    List<SubProductCategory> findAllByUpperProductCategoryIsNull();

}
