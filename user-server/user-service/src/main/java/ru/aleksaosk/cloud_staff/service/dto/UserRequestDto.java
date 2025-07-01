package ru.aleksaosk.cloud_staff.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "name cannot be empty")
    @Size(max = 100, message = "name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "lastName cannot be empty")
    @Size(max = 100, message = "lastName cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "phoneNumber cannot be empty")
    @Size(min = 10, max = 35, message = "phoneNumber must be between 10 and 35 characters")
    private String phoneNumber;

    @NotNull(message = "companyId cannot be null")
    @Positive(message = "companyId must be more than 0")
    private Long companyId;
}
