package ru.aleksaosk.cloud_staff.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
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
public class CompanyUpdateRequestDto {
    @Size(min = 3, max = 250, message = "name must be between 3 and 250 characters")
    private String name;

    @Digits(integer = 13, fraction = 2, message = "budget must have up to 13 digits before and 2 after the point")
    @DecimalMin(value = "0", message = "budget must be positive or zero")
    private BigDecimal budget;
}
