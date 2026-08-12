package com.expenseledger.expense_ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpenseLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseLedgerApplication.class, args);
	}

}
