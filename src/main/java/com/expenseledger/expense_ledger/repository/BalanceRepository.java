package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BalanceRepository extends JpaRepository<Balance, Long>{
  Optional<Balance> findByGroupIdAndOwedByIdAndOwedToId(Long groupId, Long owedById, Long owedToId);
  List<Balance> findByGroupId(Long groupId);
}
