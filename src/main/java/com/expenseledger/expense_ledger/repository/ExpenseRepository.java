package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
}
