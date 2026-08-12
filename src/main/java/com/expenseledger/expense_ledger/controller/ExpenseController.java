package com.expenseledger.expense_ledger.controller;
import com.expenseledger.expense_ledger.dto.AddExpenseRequest;
import com.expenseledger.expense_ledger.entity.Expense;
import com.expenseledger.expense_ledger.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/groups")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<Expense> addExpense(
            @PathVariable Long groupId,
            @RequestBody AddExpenseRequest request,
            Authentication authentication
    ){
        String payerEmail = authentication.getName();
        Expense expense = expenseService.addExpense(groupId, request, payerEmail);
        return ResponseEntity.ok(expense);
    }
}
