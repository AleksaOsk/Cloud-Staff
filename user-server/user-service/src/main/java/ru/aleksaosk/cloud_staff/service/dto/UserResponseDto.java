package ru.aleksaosk.cloud_staff.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String phoneNumber;
    private CompanyShortResponseDto company;
}
