package clc65.ithanhphan.cuoiki.controllers;

import clc65.ithanhphan.cuoiki.models.Tenant;
import clc65.ithanhphan.cuoiki.services.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    public String listTenants(
            Model model,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<Tenant> tenantPage = tenantService.getAllTenants(keyword, page, size);

        model.addAttribute("tenants", tenantPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tenantPage.getTotalPages());
        model.addAttribute("totalItems", tenantPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);

        return "tenants/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tenant", new Tenant());
        return "tenants/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("tenant", tenantService.getTenantById(id));
        return "tenants/form";
    }

    @PostMapping("/save")
    public String saveTenant(@ModelAttribute("tenant") Tenant tenant) {
        tenantService.saveTenant(tenant);
        return "redirect:/tenants";
    }

    @GetMapping("/delete/{id}")
    public String deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return "redirect:/tenants";
    }
}
