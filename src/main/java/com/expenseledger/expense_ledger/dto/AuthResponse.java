package com.expenseledger.expense_ledger.dto;

public class AuthResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthResponse(String token){
        this.token = token;
    }
}

