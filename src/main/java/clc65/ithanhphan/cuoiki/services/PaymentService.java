package clc65.ithanhphan.cuoiki.services;

import clc65.ithanhphan.cuoiki.models.Payment;
import clc65.ithanhphan.cuoiki.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Page<Payment> getAllPayments(String keyword, String method, int page, int size) {
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String cleanMethod = (method != null && !method.trim().isEmpty()) ? method.trim() : null;

        Pageable pageable = PageRequest.of(page, size);
        return paymentRepository.searchPayments(cleanKeyword, cleanMethod, pageable);
    }
}
