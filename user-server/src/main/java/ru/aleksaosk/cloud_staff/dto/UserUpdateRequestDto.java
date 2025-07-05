package ru.aleksaosk.cloud_staff.dto;

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
public class UserUpdateRequestDto {
    @Size(max = 100, message = "name cannot exceed 100 characters")
    private String name;

    @Size(max = 100, message = "lastName cannot exceed 100 characters")
    private String lastName;

    @Size(min = 9, max = 30, message = "phoneNumber must be between 9 and 30 characters")
    private String phoneNumber;

    @Positive(message = "companyId must be more than 0")
    private Long companyId;
}
