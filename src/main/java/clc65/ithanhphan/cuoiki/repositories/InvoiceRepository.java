package clc65.ithanhphan.cuoiki.repositories;

import clc65.ithanhphan.cuoiki.models.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT i FROM Invoice i JOIN i.contract c JOIN c.tenant t JOIN c.room r " +
            "WHERE (:keyword IS NULL OR i.invoiceCode LIKE %:keyword% OR c.contractCode LIKE %:keyword% OR t.fullName LIKE %:keyword% OR r.roomName LIKE %:keyword%) " +
            "AND (:month IS NULL OR i.billingMonth = :month) " +
            "AND (:status IS NULL OR i.status = :status)",
            countQuery = "SELECT COUNT(i) FROM Invoice i JOIN i.contract c JOIN c.tenant t JOIN c.room r " +
                    "WHERE (:keyword IS NULL OR i.invoiceCode LIKE %:keyword% OR c.contractCode LIKE %:keyword% OR t.fullName LIKE %:keyword% OR r.roomName LIKE %:keyword%) " +
                    "AND (:month IS NULL OR i.billingMonth = :month) " +
                    "AND (:status IS NULL OR i.status = :status)")
    Page<Invoice> searchInvoices(@Param("keyword") String keyword,
                                 @Param("month") Integer month,
                                 @Param("status") Invoice.InvoiceStatus status,
                                 Pageable pageable);

    long countByStatus(Invoice.InvoiceStatus status);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PAID' " +
            "AND i.billingMonth = :month AND i.billingYear = :year")
    BigDecimal sumRevenueByMonthAndYear(@Param("month") int month, @Param("year") int year);

}