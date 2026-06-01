package clc65.ithanhphan.cuoiki.controllers;

import clc65.ithanhphan.cuoiki.models.Contract;
import clc65.ithanhphan.cuoiki.models.ContractStatus;
import clc65.ithanhphan.cuoiki.repositories.RoomRepository;
import clc65.ithanhphan.cuoiki.repositories.TenantRepository;
import clc65.ithanhphan.cuoiki.services.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;

    @GetMapping
    public String listContracts(
            Model model,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Page<Contract> contractPage = contractService.getAllContracts(keyword, page, size);

        model.addAttribute("contracts", contractPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contractPage.getTotalPages());
        model.addAttribute("totalItems", contractPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);

        return "contracts/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Contract contract = new Contract();
        contract.setContractCode("HD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        model.addAttribute("contract", contract);
        model.addAttribute("rooms", roomRepository.findAll());
        model.addAttribute("tenants", tenantRepository.findAll());
        model.addAttribute("statuses", ContractStatus.values());

        return "contracts/form";
    }

    @PostMapping("/save")
    public String saveContract(@ModelAttribute("contract") Contract contract) {
        contractService.saveContract(contract);
        return "redirect:/contracts";
    }

    @GetMapping("/terminate/{id}")
    public String terminateContract(@PathVariable Long id) {
        contractService.terminateContract(id);
        return "redirect:/contracts";
    }
}