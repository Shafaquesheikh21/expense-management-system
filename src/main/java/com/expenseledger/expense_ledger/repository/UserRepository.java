package com.expenseledger.expense_ledger.repository;
import java.util.Optional;
import com.expenseledger.expense_ledger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
