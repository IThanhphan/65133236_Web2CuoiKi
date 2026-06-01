package clc65.ithanhphan.cuoiki.controllers;

import clc65.ithanhphan.cuoiki.models.Payment;
import clc65.ithanhphan.cuoiki.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String listPayments(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "method", required = false) String method,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        int pageSize = 10; // Mỗi trang hiển thị 10 lịch sử giao dịch
        Page<Payment> paymentPage = paymentService.getAllPayments(keyword, method, page, pageSize);

        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("method", method);

        return "payments/list";
    }
}
