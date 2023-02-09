package com.example.demo.Repository;

import com.example.demo.model.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Products, Integer> {
    @Modifying
    @Transactional
    @Query("update Products p set p.product_name =:product_name, p.description =:desc, p.sku =:sku, p.manufacturer =:manufacturer, p.product_qty =:product_qty where p.id =:id")
    public int update(@Param("product_name") String product_name, @Param("desc") String desc, @Param("sku") String sku, @Param("manufacturer") String manufacturer,@Param("product_qty") int product_qty, @Param("id") int id);

    @Query("select p from Products p where p.id=:id")
    public Products getById(int id);
}
