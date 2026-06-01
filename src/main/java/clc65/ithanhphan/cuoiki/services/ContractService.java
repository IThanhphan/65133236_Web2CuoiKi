package clc65.ithanhphan.cuoiki.services;

import clc65.ithanhphan.cuoiki.models.Contract;
import clc65.ithanhphan.cuoiki.models.ContractStatus;
import clc65.ithanhphan.cuoiki.models.Room;
import clc65.ithanhphan.cuoiki.models.RoomStatus;
import clc65.ithanhphan.cuoiki.repositories.ContractRepository;
import clc65.ithanhphan.cuoiki.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final RoomRepository roomRepository;

    public Page<Contract> getAllContracts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (keyword != null && !keyword.trim().isEmpty()) {
            return contractRepository.searchContracts(keyword, pageable);
        }
        return contractRepository.findAllContractsWithRelations(pageable);
    }

    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hợp đồng ID: " + id));
    }

    @Transactional
    public void saveContract(Contract contract) {
        boolean isNew = (contract.getId() == null);
        contractRepository.save(contract);

        if (isNew && contract.getStatus() == ContractStatus.ACTIVE) {
            Room room = contract.getRoom();
            room.setStatus(RoomStatus.RENTED);
            roomRepository.save(room);
        }
    }

    @Transactional
    public void terminateContract(Long id) {
        Contract contract = getContractById(id);
        contract.setStatus(ContractStatus.TERMINATED);
        contractRepository.save(contract);

        Room room = contract.getRoom();
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);
    }
}
