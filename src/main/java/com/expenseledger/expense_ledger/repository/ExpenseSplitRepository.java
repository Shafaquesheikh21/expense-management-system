package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long>{
    List<ExpenseSplit> findByExpenseId(Long expenseId);
}
