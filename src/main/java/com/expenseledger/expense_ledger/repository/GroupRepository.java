package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GroupRepository extends JpaRepository<Group, Long>{
}
