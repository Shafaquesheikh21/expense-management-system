package com.expenseledger.expense_ledger.dto;
import java.math.BigDecimal;
public class SplitDetail {
    private Long userId;
    private BigDecimal value;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
