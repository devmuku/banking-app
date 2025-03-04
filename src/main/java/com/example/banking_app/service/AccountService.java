package com.example.banking_app.service;

import com.example.banking_app.exceptions.AccountNotFoundException;
import com.example.banking_app.exceptions.NotEnoughBalanceException;
import com.example.banking_app.model.Account;
import com.example.banking_app.model.AccountHistory;
import com.example.banking_app.model.Action;
import com.example.banking_app.dto.TransferDTO;
import com.example.banking_app.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Transactional
    public Account deposit(String accountNr, BigDecimal amount) {
        Optional<Account> accountOptional = repository.findByNumber(accountNr);
        Account account;
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            account.setBalance(account.getBalance().add(amount));
        } else {
            account = new Account();
            account.setNumber(accountNr);
            account.setBalance(amount);
        }
        account.getHistories().add(createAccountHistory(amount, Action.DEPOSITED));
        return repository.save(account);
    }

    @Transactional
    public Account withdraw(String accountNr, BigDecimal amount) {
        Account account = getAccount(accountNr);
        BigDecimal accountAmount = account.getBalance();
        if (accountAmount.compareTo(amount) >= 1) {
            account.setBalance(account.getBalance().subtract(amount));
            account.getHistories().add(createAccountHistory(amount, Action.RETRIEVED));
            return repository.save(account);
        } else {
            throw new NotEnoughBalanceException("Not enough balance on your account");
        }
    }

    @Transactional
    public Account transfer (TransferDTO transfer) {
        Account sourceAccount = getAccount(transfer.getSourceAccountNr());
        Account targetAccount = getAccount(transfer.getTargetAccountNr());
        BigDecimal amount = transfer.getAmount();

        BigDecimal sourceAmount = sourceAccount.getBalance();
        if (sourceAmount.compareTo(amount) >= 0) {
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
            targetAccount.getHistories().add(createAccountHistory(amount, Action.RECEIVED));
            repository.save(targetAccount);
            sourceAccount.setBalance(sourceAmount.subtract(amount));
            sourceAccount.getHistories().add(createAccountHistory(amount, Action.TRANSFERED));
            return repository.save(sourceAccount);
        } else {
            throw new NotEnoughBalanceException("Not enough balance on your account");
        }
    }

    public Set<AccountHistory> getAccountHistory(String accountNr){
        return getAccount(accountNr).getHistories();
    }

    private AccountHistory createAccountHistory(BigDecimal amount, Action action) {
        AccountHistory accountHistory = new AccountHistory();
        accountHistory.setAmount(amount);
        accountHistory.setAction(action);
        accountHistory.setUpdatedAt(new Date());
        return accountHistory;
    }

    public Account getAccount(String accountNr) {
        return repository.findByNumber(accountNr).orElseThrow(
                () -> new AccountNotFoundException("Account: "+ accountNr +" does not exist"));
    }
}
