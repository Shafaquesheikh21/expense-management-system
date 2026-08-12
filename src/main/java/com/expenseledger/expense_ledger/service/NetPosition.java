package com.expenseledger.expense_ledger.service;
import com.expenseledger.expense_ledger.entity.User;
import java.math.BigDecimal;
public class NetPosition {
    private User user;
    private BigDecimal amount;
    public NetPosition(User user, BigDecimal amount){
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
