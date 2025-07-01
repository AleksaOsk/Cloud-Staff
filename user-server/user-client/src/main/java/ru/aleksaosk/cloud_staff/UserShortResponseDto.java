package ru.aleksaosk.cloud_staff;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShortResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String phoneNumber;
}
