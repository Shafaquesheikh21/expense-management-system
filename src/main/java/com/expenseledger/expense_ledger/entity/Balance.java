package com.expenseledger.expense_ledger.entity;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "balances",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "owed_by", "owed_to"}))
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "owed_by")
    private User owedBy;

    @ManyToOne
    @JoinColumn(name = "owed_to")
    private User owedTo;

    @Column(nullable = false)
    private BigDecimal amount;

    @Version
    private Integer version;

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

    public User getOwedBy() {
        return owedBy;
    }

    public void setOwedBy(User owedBy) {
        this.owedBy = owedBy;
    }

    public User getOwedTo() {
        return owedTo;
    }

    public void setOwedTo(User owedTo) {
        this.owedTo = owedTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
