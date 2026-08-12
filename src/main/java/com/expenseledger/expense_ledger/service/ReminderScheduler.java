package com.expenseledger.expense_ledger.service;
import com.expenseledger.expense_ledger.entity.PersonalExpense;
import com.expenseledger.expense_ledger.repository.PersonalExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
@Component
public class ReminderScheduler
{
    @Autowired
    private PersonalExpenseRepository personalExpenseRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkOverdueExpenses(){
        LocalDate today = LocalDate.now();
        List<PersonalExpense> dueOrOverdue = personalExpenseRepository.findByNextDueDateLessThanEqual(today);
        for(PersonalExpense expense: dueOrOverdue){
            if(!Boolean.TRUE.equals(expense.getIsOverDue())){
                expense.setIsOverDue(true);
                personalExpenseRepository.save(expense);
                System.out.println("Marked overdue: " + expense.getTitle() + "for user " + expense.getUser().getEmail() );
            }
        }
    }
}
