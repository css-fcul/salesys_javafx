package pt.ul.fc.css.salesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.salesys.entities.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(int code);

    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :delta WHERE p.code = :code")
    void updateStock(@Param("code") int code, @Param("delta") int delta);

    // return all products sorted by code
    @Query("SELECT p FROM Product p ORDER BY p.code ASC")
    List<Product> findAllSortedByCode();



    
}
