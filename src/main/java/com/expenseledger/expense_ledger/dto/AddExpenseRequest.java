package com.expenseledger.expense_ledger.dto;
import java.math.BigDecimal;
import java.util.List;

public class AddExpenseRequest {
 private BigDecimal amount;
 private String splitType;
 private List<SplitDetail> splits;

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public List<SplitDetail> getSplits() {
        return splits;
    }

    public void setSplits(List<SplitDetail> splits) {
        this.splits = splits;
    }

    private String description;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
