package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findByGroupId(Long groupId);
    List<Settlement> findByGroupIdAndStatus(Long groupId, String status);
}
