package com.expenseledger.expense_ledger.service;
import com.expenseledger.expense_ledger.dto.CreatePersonalExpenseRequest;
import com.expenseledger.expense_ledger.entity.PersonalExpense;
import com.expenseledger.expense_ledger.entity.User;
import com.expenseledger.expense_ledger.repository.PersonalExpenseRepository;
import com.expenseledger.expense_ledger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonalExpenseService {
    @Autowired
    private PersonalExpenseRepository personalExpenseRepository;

    @Autowired
    private UserRepository userRepository;

    public PersonalExpense create(CreatePersonalExpenseRequest request, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));
        PersonalExpense expense = new PersonalExpense();
        expense.setUser(user);
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setRecurrence(request.getRecurrence());
        expense.setDueDay(request.getDueDay());
        expense.setIsActive(true);
        expense.setIsOverDue(false);
        expense.setNextDueDate(calculateFirstDueDate(request.getDueDay()));

        return personalExpenseRepository.save(expense);

    }
    public List<PersonalExpense> getUserExpense(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return personalExpenseRepository.findByUserId(user.getId());

    }
    private LocalDate calculateFirstDueDate(Integer dueDay){
        LocalDate today = LocalDate.now();
        LocalDate candidateDate = today.withDayOfMonth(Math.min(dueDay, today.lengthOfMonth()));
        if(candidateDate.isBefore(today) || candidateDate.isEqual(today)){
            candidateDate = candidateDate.plusMonths(1).withDayOfMonth(Math.min(dueDay,
                    candidateDate.plusMonths(1).lengthOfMonth()));
        }
        return candidateDate;
    }
    public PersonalExpense markAsPaid(Long expenseId){
        PersonalExpense expense = personalExpenseRepository.findById(expenseId)
                .orElseThrow(()-> new RuntimeException("Expense not found"));
        if (expense.getLastPaidDate() != null && expense.getLastPaidDate().isEqual(LocalDate.now())) {
            throw new RuntimeException("This expense has already been marked as paid today");
        }
        expense.setLastPaidDate(LocalDate.now());
        if(expense.getRecurrence().equals("WEEKLY")){
            LocalDate nextDueDate = expense.getNextDueDate().plusDays(7);
            expense.setNextDueDate(nextDueDate);
        } else if (expense.getRecurrence().equals("MONTHLY")) {
            LocalDate candidateDate = expense.getNextDueDate().plusMonths(1);
            candidateDate = candidateDate.withDayOfMonth(Math.min(expense.getDueDay(), candidateDate.lengthOfMonth()));
            expense.setNextDueDate(candidateDate);
        }
       return personalExpenseRepository.save(expense);
    }
}
