package com.expenseledger.expense_ledger.dto;

import java.math.BigDecimal;

public class BalanceView {

    private Long owesUserId;
    private String owesUserName;
    private Long isOwedUserId;
    private String isOwedUserName;
    private BigDecimal amount;

    public Long getOwesUserId() {
        return owesUserId;
    }

    public void setOwesUserId(Long owesUserId) {
        this.owesUserId = owesUserId;
    }

    public String getOwesUserName() {
        return owesUserName;
    }

    public void setOwesUserName(String owesUserName) {
        this.owesUserName = owesUserName;
    }

    public Long getIsOwedUserId() {
        return isOwedUserId;
    }

    public void setIsOwedUserId(Long isOwedUserId) {
        this.isOwedUserId = isOwedUserId;
    }

    public String getIsOwedUserName() {
        return isOwedUserName;
    }

    public void setIsOwedUserName(String isOwedUserName) {
        this.isOwedUserName = isOwedUserName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}