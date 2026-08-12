package com.expenseledger.expense_ledger.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "audit_logs")
public class AuditLog {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
@ManyToOne
    @JoinColumn(name = "actor_id")
    private User actor;
@Column(nullable = false)
    private String action;
@Column(columnDefinition = "TEXT")
    private String details;
@Column(nullable = false)
    private LocalDateTime timestamp;

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

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
