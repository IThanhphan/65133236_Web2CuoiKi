package clc65.ithanhphan.cuoiki.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_code", nullable = false, unique = true, length = 30)
    private String invoiceCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "billing_month", nullable = false)
    private Integer billingMonth;

    @Column(name = "billing_year", nullable = false)
    private Integer billingYear;

    @Column(name = "room_fee", precision = 12, scale = 2)
    private BigDecimal roomFee = BigDecimal.ZERO;

    @Column(name = "electricity_fee", precision = 12, scale = 2)
    private BigDecimal electricityFee = BigDecimal.ZERO;

    @Column(name = "water_fee", precision = 12, scale = 2)
    private BigDecimal waterFee = BigDecimal.ZERO;

    @Column(name = "internet_fee", precision = 12, scale = 2)
    private BigDecimal internetFee = BigDecimal.ZERO;

    @Column(name = "service_fee", precision = 12, scale = 2)
    private BigDecimal serviceFee = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum InvoiceStatus {
        PAID, UNPAID
    }

    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        this.totalAmount = BigDecimal.ZERO
                .add(this.roomFee != null ? this.roomFee : BigDecimal.ZERO)
                .add(this.electricityFee != null ? this.electricityFee : BigDecimal.ZERO)
                .add(this.waterFee != null ? this.waterFee : BigDecimal.ZERO)
                .add(this.internetFee != null ? this.internetFee : BigDecimal.ZERO)
                .add(this.serviceFee != null ? this.serviceFee : BigDecimal.ZERO);
    }
}