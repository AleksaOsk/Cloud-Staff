package ru.aleksaosk.cloud_staff.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDto {
    @NotBlank(message = "name cannot be empty")
    @Size(min = 3, max = 250, message = "name must be between 3 and 250 characters")
    private String name;

    @NotNull(message = "budget cannot be null")
    @Digits(integer = 13, fraction = 2, message = "budget must have up to 13 digits before and 2 after the point")
    @DecimalMin(value = "0", message = "budget must be positive or zero")
    private BigDecimal budget;
}
