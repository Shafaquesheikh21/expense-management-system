package com.expenseledger.expense_ledger.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/me")
    public String getCurrentUser(Authentication authentication){
        return "You are logged in as: " + authentication.getName();
    }

}
