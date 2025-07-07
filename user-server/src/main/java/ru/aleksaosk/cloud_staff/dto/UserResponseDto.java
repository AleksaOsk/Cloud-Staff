package ru.aleksaosk.cloud_staff.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aleksaosk.cloud_staff.manager.CompanyDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private CompanyDto company;
}
