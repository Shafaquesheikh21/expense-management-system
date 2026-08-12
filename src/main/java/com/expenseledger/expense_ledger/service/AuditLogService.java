package com.expenseledger.expense_ledger.service;
import com.expenseledger.expense_ledger.entity.AuditLog;
import com.expenseledger.expense_ledger.entity.Group;
import com.expenseledger.expense_ledger.entity.User;
import com.expenseledger.expense_ledger.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;
    public void logAction(Group group, User actor, String action, String details){
        AuditLog log = new AuditLog();
        log.setGroup(group);
        log.setActor(actor);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }
    public List<AuditLog> getGroupAuditLog(Long groupId) {
        return auditLogRepository.findByGroupIdOrderByTimestampDesc(groupId);
    }
}
