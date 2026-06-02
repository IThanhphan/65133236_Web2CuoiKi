package clc65.ithanhphan.cuoiki.services;

import clc65.ithanhphan.cuoiki.models.Invoice;
import clc65.ithanhphan.cuoiki.models.Payment;
import clc65.ithanhphan.cuoiki.repositories.InvoiceRepository;
import clc65.ithanhphan.cuoiki.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final PaymentRepository paymentRepository;

    public Page<Invoice> getAllInvoices(String keyword, Integer month, String statusStr, int page, int size) {
        Invoice.InvoiceStatus status = null;
        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                status = Invoice.InvoiceStatus.valueOf(statusStr.trim());
            } catch (IllegalArgumentException e) {
                status = null;
            }
        }

        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;

        Pageable pageable = PageRequest.of(page, size);
        return invoiceRepository.searchInvoices(cleanKeyword, month, status, pageable);
    }

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public void payInvoiceSpeedy(Long invoiceId, String paymentMethodStr) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại: " + invoiceId));

        if (invoice.getStatus() == Invoice.InvoiceStatus.UNPAID) {
            invoice.setStatus(Invoice.InvoiceStatus.PAID);
            invoiceRepository.save(invoice);

            String method = (paymentMethodStr != null && !paymentMethodStr.isEmpty()) ? paymentMethodStr : "CASH";

            Payment payment = new Payment();
            payment.setInvoice(invoice);
            payment.setAmount(invoice.getTotalAmount());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(method);

            String readableMethod = "CASH".equals(method) ? "Tiền mặt" : "Chuyển khoản";
            payment.setNote("Gạch nợ nhanh qua [" + readableMethod + "] từ trang danh sách");

            paymentRepository.save(payment);
        }
    }
}
