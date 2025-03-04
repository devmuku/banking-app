package com.example.banking_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    @NotBlank(message = "This field is required")
    private String sourceAccountNr;
    @NotBlank(message = "This field is required")
    private String targetAccountNr;
    @NotBlank(message = "This field is required")
    private BigDecimal amount;
}
