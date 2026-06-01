package clc65.ithanhphan.cuoiki.repositories;

import clc65.ithanhphan.cuoiki.models.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p JOIN p.invoice i JOIN i.contract c JOIN c.tenant t " +
            "WHERE (:keyword IS NULL OR i.invoiceCode LIKE %:keyword% OR t.fullName LIKE %:keyword%) " +
            "AND (:method IS NULL OR p.paymentMethod = :method) " +
            "ORDER BY p.paymentDate DESC")
    Page<Payment> searchPayments(@Param("keyword") String keyword,
                                 @Param("method") String method,
                                 Pageable pageable);
}
