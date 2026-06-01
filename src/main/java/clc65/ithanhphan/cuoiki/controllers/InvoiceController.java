package clc65.ithanhphan.cuoiki.controllers;

import clc65.ithanhphan.cuoiki.models.Contract;
import clc65.ithanhphan.cuoiki.models.Invoice;
import clc65.ithanhphan.cuoiki.services.ContractService;
import clc65.ithanhphan.cuoiki.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ContractService contractService;

    @GetMapping
    public String listInvoices(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        int pageSize = 5;
        Page<Invoice> invoicePage = invoiceService.getAllInvoices(keyword, month, status, page, pageSize);

        model.addAttribute("invoices", invoicePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", invoicePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("month", month);
        model.addAttribute("status", status);

        return "invoices/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Invoice invoice = new Invoice();

        // Thiết lập sẵn kỳ thu tiền là tháng và năm hiện hành
        LocalDate today = LocalDate.now();
        invoice.setBillingMonth(today.getMonthValue());
        invoice.setBillingYear(today.getYear());
        invoice.setInternetFee(new java.math.BigDecimal("100000")); // Gợi ý mặc định 100k theo form mẫu
        invoice.setServiceFee(new java.math.BigDecimal("50000"));

        List<Contract> activeContracts = contractService.getActiveContracts();

        model.addAttribute("invoice", invoice);
        model.addAttribute("contracts", activeContracts);

        return "invoices/form";
    }

    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute("invoice") Invoice invoice) {
        invoiceService.saveInvoice(invoice);
        return "redirect:/invoices";
    }

    @PostMapping("/pay-speedy/{id}")
    public String handlePaySpeedy(@PathVariable("id") Long id,
                                  @RequestParam(value = "paymentMethod", defaultValue = "CASH") String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        try {
            invoiceService.payInvoiceSpeedy(id, paymentMethod);

            String vietnameseMethod = "CASH".equals(paymentMethod) ? "Tiền mặt" : "Chuyển khoản";
            redirectAttributes.addFlashAttribute("successMessage", "Xác nhận thu tiền thành công qua: " + vietnameseMethod);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thất bại: " + e.getMessage());
        }
        return "redirect:/invoices";
    }
}
