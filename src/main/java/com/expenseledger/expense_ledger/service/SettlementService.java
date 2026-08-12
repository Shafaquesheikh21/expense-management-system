package com.expenseledger.expense_ledger.service;
import com.expenseledger.expense_ledger.dto.BalanceView;
import com.expenseledger.expense_ledger.dto.SettlementTransaction;
import com.expenseledger.expense_ledger.entity.Balance;
import com.expenseledger.expense_ledger.entity.User;
import com.expenseledger.expense_ledger.repository.BalanceRepository;
import com.expenseledger.expense_ledger.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.expenseledger.expense_ledger.entity.Settlement;
import com.expenseledger.expense_ledger.repository.GroupRepository;
import com.expenseledger.expense_ledger.repository.UserRepository;
import com.expenseledger.expense_ledger.entity.Group;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;
@Service
public class SettlementService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SettlementRepository settlementRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    public List<SettlementTransaction> simplifyDebts(Long groupId){
        List<Balance> balances = balanceRepository.findByGroupId(groupId);//get all the balance list
        Map<Long, User> userLookup = new HashMap<>(); //Creating an empty map to store user id as a key and user as a
        // value
        Map<Long, BigDecimal> netPositions = new HashMap<>(); //creating an empty map to store user id as a key and
        // their net balance as a value
       for(Balance balance: balances){
           User owes = balance.getOwedBy();
           User isOwed = balance.getOwedTo();
           userLookup.put(owes.getId(), owes);
           userLookup.put(isOwed.getId(), isOwed);
           netPositions.merge(owes.getId(), balance.getAmount().negate(), BigDecimal::add); // this will get the id
           // of the person who owes the money and the amount of that row and if the person already had a balance it
           // will just add to it and negate will make it negative (450 will become -450 as it is owing money
           netPositions.merge(isOwed.getId(), balance.getAmount(), BigDecimal::add);//here we will not negate as it
           // should be positive because the user here is owed
       }
       PriorityQueue<NetPosition> creditors = new PriorityQueue<>(
               (a,b)->b.getAmount().compareTo(a.getAmount()) // this will compare amount of b with a to get the
               // highest creditor
       );
       PriorityQueue<NetPosition> debtors = new PriorityQueue<>(
               (a, b) -> a.getAmount().compareTo(b.getAmount()) //this will get highest  debtor This time it's a
               // compared to b in the normal order — which sorts smallest-first Since debtor amounts are negative,
               // "smallest" (most negative, e.g., -500) means "owes the most" — exactly who we want to grab first.
       );
       for(Map.Entry<Long, BigDecimal> entry : netPositions.entrySet()){
           BigDecimal amount = entry.getValue();
           User user = userLookup.get(entry.getKey());
           if(amount.compareTo(BigDecimal.ZERO)>0){
               creditors.add(new NetPosition(user, amount));
           }
           else if (amount.compareTo(BigDecimal.ZERO)<0) {
            debtors.add(new NetPosition(user, amount));
           }
       }
       List<SettlementTransaction> transactions = new ArrayList<>(); // list ot collect the final results
        while(!creditors.isEmpty() && !debtors.isEmpty()){
            NetPosition biggestCreditor = creditors.poll();
            NetPosition biggestDebtor = debtors.poll();
            BigDecimal creditAmount = biggestCreditor.getAmount();
            BigDecimal debtAmount = biggestDebtor.getAmount().abs();
            BigDecimal settledAmount = creditAmount.min(debtAmount); // get the min of both the amount

            SettlementTransaction transaction = new SettlementTransaction();
            transaction.setFromUserId(biggestDebtor.getUser().getId());
            transaction.setFromUserName(biggestDebtor.getUser().getName());
            transaction.setToUserId(biggestCreditor.getUser().getId());
            transaction.setToUserName(biggestCreditor.getUser().getName());
            transaction.setAmount(settledAmount);
            transactions.add(transaction);
            BigDecimal remainingCredit = creditAmount.subtract(settledAmount);
            BigDecimal remainingDebt = debtAmount.subtract(settledAmount);

            if(remainingCredit.compareTo(BigDecimal.ZERO)>0){
                biggestCreditor.setAmount(remainingCredit);
                creditors.add(biggestCreditor);
            }
            if (remainingDebt.compareTo(BigDecimal.ZERO)>0) {
                biggestDebtor.setAmount(remainingDebt.negate());
                debtors.add(biggestDebtor);
            }
        }
        return transactions;
    }
    @Transactional
    public List<Settlement> settleUp(Long groupId){
        List<SettlementTransaction> suggestions = simplifyDebts(groupId);
        List<Settlement> savedSettlements = new ArrayList<>();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        for(SettlementTransaction suggestion : suggestions){
            User fromUser = userRepository.findById(suggestion.getFromUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            User toUser = userRepository.findById(suggestion.getToUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Settlement settlement = new Settlement();
            settlement.setGroup(group);
            settlement.setFromUser(fromUser);
            settlement.setToUser(toUser);
            settlement.setAmount(suggestion.getAmount());
            settlement.setStatus("PENDING");
            settlement.setCreatedAt(LocalDateTime.now());
            savedSettlements.add(settlementRepository.save(settlement));
        }
        return savedSettlements;
    }
 @Transactional
    public Settlement confirmSettlement(Long settlementId){
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(()-> new RuntimeException("Settlement not found"));
        if("CONFIRMED".equals(settlement.getStatus())){
            throw new RuntimeException("Settlement already Confirmed");
        }
        settlement.setStatus("CONFIRMED");
        settlement.setConfirmedAt(LocalDateTime.now());
        settlementRepository.save(settlement);
     Optional<Balance> balance = balanceRepository.findByGroupIdAndOwedByIdAndOwedToId(
             settlement.getGroup().getId(),
             settlement.getFromUser().getId(),
             settlement.getToUser().getId()
     );
     if(balance.isPresent()){
         Balance existingBalance = balance.get();
         BigDecimal newAmount = existingBalance.getAmount().subtract(settlement.getAmount());
       if(newAmount.compareTo(BigDecimal.ZERO)<=0){
          balanceRepository.delete(existingBalance);
       }
       else {
           existingBalance.setAmount(newAmount);
           balanceRepository.save(existingBalance);
       }
     }
     return settlement;
 }
 public List<BalanceView> getGroupBalances(Long groupId){
     List<Balance> balances = balanceRepository.findByGroupId(groupId);
     List<BalanceView> views = new ArrayList<>();
     for (Balance balance : balances) {

         BalanceView view = new BalanceView();
         view.setOwesUserId(balance.getOwedBy().getId());
         view.setOwesUserName(balance.getOwedBy().getName());
         view.setIsOwedUserId(balance.getOwedTo().getId());
         view.setIsOwedUserName(balance.getOwedTo().getName());
         view.setAmount(balance.getAmount());

         views.add(view);
     }

     return views;
 }
}
