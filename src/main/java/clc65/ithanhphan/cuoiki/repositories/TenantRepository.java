package clc65.ithanhphan.cuoiki.repositories;

import clc65.ithanhphan.cuoiki.models.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    @Query("SELECT t FROM Tenant t WHERE " +
            "LOWER(t.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.citizenId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Tenant> searchTenants(@Param("keyword") String keyword, Pageable pageable);

    Page<Tenant> findAll(Pageable pageable);

}
