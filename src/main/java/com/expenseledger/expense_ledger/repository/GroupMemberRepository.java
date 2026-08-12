package com.expenseledger.expense_ledger.repository;
import com.expenseledger.expense_ledger.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>{
    List<GroupMember> findByGroupId(Long groupId);
}
