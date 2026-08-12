package com.expenseledger.expense_ledger.controller;
import com.expenseledger.expense_ledger.entity.AuditLog;
import com.expenseledger.expense_ledger.entity.Settlement;
import com.expenseledger.expense_ledger.service.AuditLogService;
import java.util.List;
import com.expenseledger.expense_ledger.dto.AddMemberRequest;
import com.expenseledger.expense_ledger.dto.CreateGroupRequest;
import com.expenseledger.expense_ledger.entity.Group;
import com.expenseledger.expense_ledger.entity.GroupMember;
import com.expenseledger.expense_ledger.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.expenseledger.expense_ledger.dto.SettlementTransaction;
import com.expenseledger.expense_ledger.service.SettlementService;
import com.expenseledger.expense_ledger.dto.BalanceView;
@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;
@Autowired
private AuditLogService auditLogService;
@Autowired
private SettlementService settlementService;


    @PostMapping
    public ResponseEntity<Group> createGroup(
            @RequestBody CreateGroupRequest request, Authentication authentication
    ){
        String creatorEmail = authentication.getName();
        Group group = groupService.createGroup(request, creatorEmail);
        return ResponseEntity.ok(group);
    }
    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupMember> addMember(
            @PathVariable Long groupId,
            @RequestBody AddMemberRequest request
    ) {
        GroupMember member = groupService.addMember(groupId, request);
        return ResponseEntity.ok(member);
    }
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMember>> getMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getGroupMembers(groupId));
    }
    @GetMapping("/{groupId}/audit-log")
    public ResponseEntity<List<AuditLog>> getAuditLog(@PathVariable Long groupId) {
        return ResponseEntity.ok(auditLogService.getGroupAuditLog(groupId));
    }
    @GetMapping("/{groupId}/simplify-debts")
    public ResponseEntity<List<SettlementTransaction>> simplifyDebts(@PathVariable Long groupId){
        return ResponseEntity.ok(settlementService.simplifyDebts(groupId));
    }
    @GetMapping("/{groupId}/balances")
    public ResponseEntity<List<BalanceView>> getBalances(@PathVariable Long groupId) {
        return ResponseEntity.ok(settlementService.getGroupBalances(groupId));
    }

    @PostMapping("/{groupId}/settle-up")
    public ResponseEntity<List<Settlement>> settleUp(@PathVariable Long groupId){
        return ResponseEntity.ok(settlementService.settleUp(groupId));
    }
}


