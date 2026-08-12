package com.expenseledger.expense_ledger.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "Created_by")
    private User created_By;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreated_By() {
        return created_By;
    }

    public void setCreated_By(User created_By) {
        this.created_By = created_By;
    }
}
