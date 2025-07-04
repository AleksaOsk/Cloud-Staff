package ru.aleksaosk.cloud_staff.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aleksaosk.cloud_staff.manager.UserDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDto {
    private Long id;
    private String name;
    private BigDecimal budget;
    private List<UserDto> users;
}
