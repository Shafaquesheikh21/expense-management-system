package com.expenseledger.expense_ledger.controller;
import com.expenseledger.expense_ledger.entity.Settlement;
import com.expenseledger.expense_ledger.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {
    @Autowired
    private SettlementService settlementService;

    @PostMapping("/{settlementId}/confirm")
    public ResponseEntity<Settlement> confirmSettlement(@PathVariable Long settlementId) {
        return ResponseEntity.ok(settlementService.confirmSettlement(settlementId));
    }
}
