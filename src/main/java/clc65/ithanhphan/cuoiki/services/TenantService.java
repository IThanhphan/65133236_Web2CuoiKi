package clc65.ithanhphan.cuoiki.services;

import clc65.ithanhphan.cuoiki.models.Tenant;
import clc65.ithanhphan.cuoiki.repositories.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    public Page<Tenant> getAllTenants(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if (keyword != null && !keyword.trim().isEmpty()) {
            return tenantRepository.searchTenants(keyword, pageable);
        }
        return tenantRepository.findAll(pageable);
    }

    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người thuê có ID: " + id));
    }

    public void saveTenant(Tenant tenant) {
        tenantRepository.save(tenant);
    }

    public void deleteTenant(Long id) {
        tenantRepository.deleteById(id);
    }
}
