package pt.ul.fc.css.salesys.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.salesys.entities.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Sale s WHERE s.customer.id = :id AND s.status = :status")
    boolean openSaleByCustomer(@Param("id") Long id, @Param("status") Sale.SaleStatus status);

    @Query("SELECT s FROM Sale s WHERE s.customer.id = :id")
    Optional<Sale> findByCustomerId(@Param("id") Long id);

}
