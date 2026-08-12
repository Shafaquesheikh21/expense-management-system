package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{
    List<AuditLog> findByGroupIdOrderByTimestampDesc(Long groupId);
}
