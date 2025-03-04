package com.example.banking_app.dto;

import com.example.banking_app.model.AccountHistory;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.Set;

@Setter
@Getter
@Accessors(chain = true)
public class AccountHistoryDTO {

    @NotBlank(message = "This field is required")
    private Set<AccountHistory> account_history;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AccountHistoryDTO(@JsonProperty("account_history") Set<AccountHistory> history) {
        this.account_history = history;
    }
}
