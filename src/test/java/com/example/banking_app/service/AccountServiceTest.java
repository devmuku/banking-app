package com.example.banking_app.service;

import com.example.banking_app.exceptions.AccountNotFoundException;
import com.example.banking_app.exceptions.NotEnoughBalanceException;
import com.example.banking_app.model.Account;
import com.example.banking_app.dto.TransferDTO;
import com.example.banking_app.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private static final String ACCOUNT_NUMBER1 = "234232";
    private static final String ACCOUNT_NUMBER2 = "113344";

    @Test
    public void testWithdraw() {

        Optional<Account> account = Optional.of(new Account(1L, ACCOUNT_NUMBER1, new BigDecimal(5000), new HashSet<>()));
        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(account);

        accountService.withdraw(ACCOUNT_NUMBER1, new BigDecimal(4000));

        ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(argument.capture());
        assertEquals(0, argument.getValue().getBalance().compareTo(new BigDecimal(1000)));
        assertEquals(ACCOUNT_NUMBER1, argument.getValue().getNumber());
    }

    @Test
    public void testWithdrawWithNotEnoughBalance() {
        Optional<Account> account = Optional.of(new Account(1L, ACCOUNT_NUMBER1, new BigDecimal(5000), new HashSet<>()));
        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(account);

        assertThrows(NotEnoughBalanceException.class, () -> {
            accountService.withdraw(ACCOUNT_NUMBER1, new BigDecimal(5001));
        });
    }

    @Test
    public void testWithdrawWithNotExistingAccount() {
        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.withdraw(ACCOUNT_NUMBER1, new BigDecimal(5001));
        });
    }

    @Test
    public void testDeposit() {

        Optional<Account> account = Optional.of(new Account(1L, ACCOUNT_NUMBER1, new BigDecimal(5000), new HashSet<>()));
        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(account);

        accountService.deposit(ACCOUNT_NUMBER1, new BigDecimal(4000));

        ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(argument.capture());
        assertEquals(0, argument.getValue().getBalance().compareTo(new BigDecimal(9000)));
        assertEquals(ACCOUNT_NUMBER1, argument.getValue().getNumber());
    }

    @Test
    public void testDepositOnNotExistingAccount() {

        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(Optional.empty());

        accountService.deposit(ACCOUNT_NUMBER1, new BigDecimal(4000));

        ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(argument.capture());
        assertEquals(0, argument.getValue().getBalance().compareTo(new BigDecimal(4000)));
        assertEquals(ACCOUNT_NUMBER1, argument.getValue().getNumber());
    }

    @Test
    public void testTransfer() {

        Optional<Account> sourceAccount = Optional.of(new Account(1L, ACCOUNT_NUMBER1, new BigDecimal(5000), new HashSet<>()));
        Optional<Account> targetAccount = Optional.of(new Account(1L, ACCOUNT_NUMBER2, new BigDecimal(0), new HashSet<>()));
        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(sourceAccount);
        when(accountRepository.findByNumber(ACCOUNT_NUMBER2)).thenReturn(targetAccount);

        InOrder inOrder = inOrder(accountRepository);

        accountService.transfer(new TransferDTO(ACCOUNT_NUMBER1, ACCOUNT_NUMBER2, new BigDecimal(4000)));

        inOrder.verify(accountRepository).save(argThat((arg) -> arg.getBalance().compareTo(new BigDecimal(4000))==0
                                                                     && arg.getNumber().equals(ACCOUNT_NUMBER2)));
        inOrder.verify(accountRepository).save(argThat((arg) -> arg.getBalance().compareTo(new BigDecimal(1000))==0
                                                                     && arg.getNumber().equals(ACCOUNT_NUMBER1)));
    }

    @Test
    public void testTransferWithNotEnoughBalanceOnSourceAccount() {

        Optional<Account> sourceAccount = Optional.of(new Account(1L, ACCOUNT_NUMBER1, new BigDecimal(2000), new HashSet<>()));
        Optional<Account> targetAccount = Optional.of(new Account(1L, ACCOUNT_NUMBER2, new BigDecimal(0), new HashSet<>()));
        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(sourceAccount);
        when(accountRepository.findByNumber(ACCOUNT_NUMBER2)).thenReturn(targetAccount);

        assertThrows(NotEnoughBalanceException.class, () -> {
            accountService.transfer(new TransferDTO(ACCOUNT_NUMBER1, ACCOUNT_NUMBER2, new BigDecimal(5000)));
        });
    }

    @Test
    public void testTransferWithNotExistingTargetAccount() {
        Optional<Account> sourceAccount = Optional.of(new Account(1L, ACCOUNT_NUMBER1, new BigDecimal(0), new HashSet<>()));
        when(accountRepository.findByNumber(ACCOUNT_NUMBER1)).thenReturn(sourceAccount);
        when(accountRepository.findByNumber(ACCOUNT_NUMBER2)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(new TransferDTO(ACCOUNT_NUMBER1, ACCOUNT_NUMBER2, new BigDecimal(5000)));
        });
    }

}
