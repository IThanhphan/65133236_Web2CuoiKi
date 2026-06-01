package clc65.ithanhphan.cuoiki.repositories;

import clc65.ithanhphan.cuoiki.models.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c JOIN FETCH c.room JOIN FETCH c.tenant WHERE " +
            "LOWER(c.contractCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.tenant.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.room.roomCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Contract> searchContracts(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT c FROM Contract c JOIN FETCH c.room JOIN FETCH c.tenant",
            countQuery = "SELECT count(c) FROM Contract c")
    Page<Contract> findAllContractsWithRelations(Pageable pageable);
}