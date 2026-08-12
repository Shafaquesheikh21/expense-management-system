package com.expenseledger.expense_ledger.service;
import com.expenseledger.expense_ledger.dto.AddExpenseRequest;
import com.expenseledger.expense_ledger.dto.SplitDetail;
import com.expenseledger.expense_ledger.entity.Expense;
import com.expenseledger.expense_ledger.entity.ExpenseSplit;
import com.expenseledger.expense_ledger.entity.Group;
import com.expenseledger.expense_ledger.entity.GroupMember;
import com.expenseledger.expense_ledger.entity.User;
import com.expenseledger.expense_ledger.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.expenseledger.expense_ledger.entity.Balance;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
@Service
public class ExpenseService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public Expense addExpense(Long groupId, AddExpenseRequest request, String payerEmail) {

        Group group = groupRepository.findById(groupId).
                orElseThrow(() -> new RuntimeException("Group not found"));
        User payer = userRepository.findByEmail(payerEmail).orElseThrow(() -> new RuntimeException("User Not found"));

        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setPaidBy(payer);
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setSplitType(request.getSplitType());

        Expense savedExpense = expenseRepository.save(expense);
        BigDecimal totalAmount = request.getAmount();
        String splitType = request.getSplitType();
        if ("PERCENTAGE".equals(splitType)) {
            for (SplitDetail detail : request.getSplits()) {
                User user = userRepository.findById(detail.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                BigDecimal shareAmount = totalAmount
                        .multiply(detail.getValue())
                        .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_FLOOR);
                 saveSplitAndBalance(savedExpense, user, shareAmount, group, payer);
            }

        } else if ("WEIGHTED".equals(splitType)) {
            BigDecimal totalWeight = BigDecimal.ZERO;
            for (SplitDetail detail : request.getSplits()) {
                totalWeight = totalWeight.add(detail.getValue());
            }
            for (SplitDetail detail : request.getSplits()) {
                User user = userRepository.findById(detail.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                BigDecimal shareAmount = totalAmount
                        .multiply(detail.getValue())
                        .divide(totalWeight, 2, BigDecimal.ROUND_FLOOR);
                saveSplitAndBalance(savedExpense, user, shareAmount, group, payer);
            }

        } else {


            List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
            int memberCount = members.size();

            BigDecimal equalShare =
                    totalAmount.divide(BigDecimal.valueOf(memberCount), 10, BigDecimal.ROUND_FLOOR).setScale(2,
                            BigDecimal.ROUND_FLOOR);


            BigDecimal distributedTotal = equalShare.multiply(BigDecimal.valueOf(memberCount));
            BigDecimal remainder = totalAmount.subtract(distributedTotal);

            for (int i = 0; i < memberCount; i++) {
                GroupMember member = members.get(i);
                BigDecimal shareForThisPerson = equalShare;

                if (i == 0) {
                    shareForThisPerson = shareForThisPerson.add(remainder);

                }
                 saveSplitAndBalance(savedExpense, member.getUser(), shareForThisPerson, group, payer);
            }
        }
        auditLogService.logAction(group, payer, "EXPENSE_ADDED",
                payer.getName() + " added an expense of ₹" + totalAmount + " for '" + request.getDescription() + "'");
        return savedExpense;
    }
    private void saveSplitAndBalance(Expense expense, User user, BigDecimal shareAmount, Group group, User payer){
            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(expense);
            split.setUser(user);
            split.setShareAmount(shareAmount);

            expenseSplitRepository.save(split);
            updateBalance(group, user, payer, shareAmount);
        }
    private void updateBalance(Group group, User owes, User isOwed, BigDecimal amount){
        if(owes.getId().equals(isOwed.getId())){
            return;
        }
        int maxRetries =5;
        int attempt =0;
    while(attempt < maxRetries){
        try{
            Optional<Balance> existing = balanceRepository.findByGroupIdAndOwedByIdAndOwedToId(group.getId(),
                    owes.getId(), isOwed.getId());
            if(existing.isPresent()){
                Balance balance = existing.get();
                balance.setAmount(balance.getAmount().add(amount));
                balanceRepository.save(balance);
            }
            else{
                Balance newBalance = new Balance();
                newBalance.setGroup(group);
                newBalance.setOwedBy(owes);
                newBalance.setOwedTo(isOwed);
                newBalance.setAmount(amount);
                balanceRepository.save(newBalance);

            }
            return;
        }
        catch (ObjectOptimisticLockingFailureException | DataIntegrityViolationException e){
            attempt++;
            if(attempt>=maxRetries){
                throw new RuntimeException("Failed to update balance after multiple attempts, please try again");
            }
        }
    }

    }

}
