package com.example.banking_app.controller;

import com.example.banking_app.model.Account;
import com.example.banking_app.model.AccountHistory;
import com.example.banking_app.model.Action;
import com.example.banking_app.dto.TransferDTO;
import com.example.banking_app.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.math.BigDecimal;
import java.util.Date;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account account1;
    private AccountHistory accountHistory1;
    private Account account2;

    final private String ACCOUNT_NR = "23423423";

    @BeforeEach
    public void setup() {

        account1 = new Account();
        account1.setNumber(ACCOUNT_NR);
        account1.setBalance(new BigDecimal(26000));
        accountHistory1 = new AccountHistory();
        accountHistory1.setAmount(new BigDecimal(2000));
        accountHistory1.setAction(Action.DEPOSITED);
        accountHistory1.setUpdatedAt(new Date());
        account1.getHistories().add(accountHistory1);

        account2 = new Account();
        account2.setNumber("76767656");
        account2.setBalance(new BigDecimal(43000));

    }

    @Test
    public void getAccountBalanceTest() throws Exception {
        given(accountService.getAccount(anyString())).willReturn(account1);

        mockMvc.perform(get("/accounts/{accountNr}/balance", account1.getNumber())
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.account.number", is(account1.getNumber())))
               .andExpect(jsonPath("$.account.balance", is(account1.getBalance().intValue())));
    }

    @Test
    public void depositTest() throws Exception {
        given(accountService.deposit(ACCOUNT_NR, new BigDecimal(2000))).willReturn(account1);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountNr", ACCOUNT_NR);
        requestParams.add("amount", "2000");

        mockMvc.perform(put("/accounts/deposit")
               .params(requestParams)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.account.number", is(account1.getNumber())))
               .andExpect(jsonPath("$.account.balance", is(account1.getBalance().intValue())));
    }

    @Test
    public void withdrawTest() throws Exception {
        given(accountService.withdraw(ACCOUNT_NR, new BigDecimal(1000))).willReturn(account1);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountNr", ACCOUNT_NR);
        requestParams.add("amount", "1000");

        mockMvc.perform(put("/accounts/withdraw")
                    .params(requestParams)
                    .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.account.number", is(account1.getNumber())))
               .andExpect(jsonPath("$.account.balance", is(account1.getBalance().intValue())));
    }

    @Test
    public void getAccountHistoryTest() throws Exception {
        given(accountService.getAccountHistory(account1.getNumber())).willReturn(account1.getHistories());

        mockMvc.perform(get("/accounts/{accountNr}/history", account1.getNumber())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_history[0].amount", is(accountHistory1.getAmount().intValue())))
                .andExpect(jsonPath("$.account_history[0].action", is(accountHistory1.getAction().toString())));
    }

    @Test
    public void transferTest() throws Exception {
        TransferDTO transfer = new TransferDTO(account1.getNumber(), account2.getNumber(), new BigDecimal(1000));
        given(accountService.transfer(any(TransferDTO.class))).willReturn(account1);

        mockMvc.perform(put("/accounts/transfer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transfer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.number", is(account1.getNumber())))
                .andExpect(jsonPath("$.account.balance", is(account1.getBalance().intValue())));
    }

}
