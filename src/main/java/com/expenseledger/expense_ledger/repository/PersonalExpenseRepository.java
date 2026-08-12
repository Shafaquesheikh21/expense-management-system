package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.PersonalExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PersonalExpenseRepository extends JpaRepository<PersonalExpense, Long>{
    List<PersonalExpense> findByUserId(Long userId);
    List<PersonalExpense> findByNextDueDateLessThanEqual(LocalDate date);
}
