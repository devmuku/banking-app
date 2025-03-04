package com.example.banking_app.repository;

import com.example.banking_app.model.Account;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.assertj.core.api.Assertions;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    final private String ACCOUNT_NR = "232323";

    @Test
    @DisplayName("Test 1:Save Employee Test")
    @Order(1)
    @Rollback(value = false)
    public void saveAccontTest() {
        Account account = new Account();
        account.setNumber(ACCOUNT_NR);
        account.setBalance(new BigDecimal(1000));
        accountRepository.save(account);
        Assertions.assertThat(account.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getAccountTest(){
        Account account = accountRepository.findById(1L).get();
        Assertions.assertThat(account.getId()).isEqualTo(1L);
        Assertions.assertThat(account.getNumber()).isEqualTo(ACCOUNT_NR);
    }

    @Test
    @Order(3)
    public void findByNumberTest(){
        Account account = accountRepository.findByNumber(ACCOUNT_NR).get();
        Assertions.assertThat(account.getId()).isEqualTo(1L);
        assertEquals(0, account.getBalance().compareTo(new BigDecimal(1000)));
    }
}
