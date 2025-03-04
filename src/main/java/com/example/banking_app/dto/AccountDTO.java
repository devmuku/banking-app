package com.example.banking_app.dto;

import com.example.banking_app.model.Account;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AccountDTO {

    @NotBlank(message = "This field is required")
    private Account account;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AccountDTO(@JsonProperty("account") Account account) {
        this.account = account;
    }
}
