package com.example.banking_app.controller;

import com.example.banking_app.dto.TransferDTO;
import com.example.banking_app.dto.AccountHistoryDTO;
import com.example.banking_app.dto.AccountDTO;
import com.example.banking_app.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @PutMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit (@RequestParam String accountNr,
                                               @RequestParam BigDecimal amount) {
        AccountDTO account = new AccountDTO(service.deposit(accountNr, amount));
        return ResponseEntity.ok(account);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw (@RequestParam String accountNr,
                                                @RequestParam BigDecimal amount) {
        AccountDTO account = new AccountDTO(service.withdraw(accountNr, amount));
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNr}/balance")
    public ResponseEntity<AccountDTO> getAccountBalance (@PathVariable String accountNr) {
        AccountDTO account = new AccountDTO(service.getAccount(accountNr));
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNr}/history")
    public ResponseEntity<AccountHistoryDTO> getAccountHistory (@PathVariable String accountNr) {
        AccountHistoryDTO accountHistory = new AccountHistoryDTO(service.getAccountHistory(accountNr));
        return ResponseEntity.ok(accountHistory);
    }

    @PutMapping("/transfer")
    public ResponseEntity<AccountDTO> transfer (@RequestBody TransferDTO transfer) {
        AccountDTO account = new AccountDTO(service.transfer(transfer));
        return ResponseEntity.ok(account);
    }

}
