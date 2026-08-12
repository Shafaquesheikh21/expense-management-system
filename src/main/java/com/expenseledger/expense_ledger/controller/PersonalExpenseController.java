package com.expenseledger.expense_ledger.controller;

import com.expenseledger.expense_ledger.dto.CreatePersonalExpenseRequest;
import com.expenseledger.expense_ledger.entity.PersonalExpense;
import com.expenseledger.expense_ledger.service.PersonalExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personal-expenses")
public class PersonalExpenseController {
    @Autowired
    private PersonalExpenseService personalExpenseService;

    @PostMapping
    public ResponseEntity<PersonalExpense> createExpense(
            @RequestBody CreatePersonalExpenseRequest request,
            Authentication authentication){
        String email = authentication.getName();
        PersonalExpense expense = personalExpenseService.create(request, email);
        return ResponseEntity.ok(expense);
    }
    @GetMapping
    public ResponseEntity<List<PersonalExpense>> getPersonalExpenses(
            Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(personalExpenseService.getUserExpense(email));
    }

    @PostMapping("/{expenseId}/mark-paid")
    public ResponseEntity<PersonalExpense> markAsPaid(@PathVariable Long expenseId) {
        return ResponseEntity.ok(personalExpenseService.markAsPaid(expenseId));
    }

}
