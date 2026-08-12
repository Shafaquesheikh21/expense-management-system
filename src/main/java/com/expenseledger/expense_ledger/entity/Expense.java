package com.expenseledger.expense_ledger.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "expenses")
public class Expense {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
   @ManyToOne
    @JoinColumn(name = "paid_by")
    private User paidBy;
   @Column(nullable = false)
    private BigDecimal amount;
   @Column(nullable = false)
    private String description;

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    @Column(nullable = false)
   private String splitType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(User paidBy) {
        this.paidBy = paidBy;
    }

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
