package clc65.ithanhphan.cuoiki.services;

import clc65.ithanhphan.cuoiki.models.ContractStatus;
import clc65.ithanhphan.cuoiki.models.Invoice;
import clc65.ithanhphan.cuoiki.models.RoomStatus;
import clc65.ithanhphan.cuoiki.repositories.ContractRepository;
import clc65.ithanhphan.cuoiki.repositories.InvoiceRepository;
import clc65.ithanhphan.cuoiki.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoomRepository roomRepository;
    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalRooms = roomRepository.count();

        long occupiedRooms = roomRepository.countByStatus(RoomStatus.RENTED);
        long availableRooms = roomRepository.countByStatus(RoomStatus.AVAILABLE);

        long activeContracts = contractRepository.countByStatus(ContractStatus.ACTIVE);

        long unpaidInvoices = invoiceRepository.countByStatus(Invoice.InvoiceStatus.UNPAID);

        LocalDate today = LocalDate.now();
        BigDecimal currentMonthRevenue = invoiceRepository.sumRevenueByMonthAndYear(today.getMonthValue(), today.getYear());
        if (currentMonthRevenue == null) {
            currentMonthRevenue = BigDecimal.ZERO;
        }

        stats.put("totalRooms", totalRooms);
        stats.put("occupiedRooms", occupiedRooms);
        stats.put("availableRooms", availableRooms);
        stats.put("activeContracts", activeContracts);
        stats.put("unpaidInvoices", unpaidInvoices);
        stats.put("currentMonthRevenue", currentMonthRevenue);

        return stats;
    }
}
