package ru.aleksaosk.cloud_staff.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapToUser(UserRequestDto request, String phoneNumber) {
        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(phoneNumber);
        user.setCompanyId(request.getCompanyId());
        return user;
    }

    public static UserResponseDto mapToUserResponseDto(User user, CompanyShortResponseDto company) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        userResponseDto.setCompany(company);
        return userResponseDto;
    }

    public static UserShortResponseDto mapToUserShortResponseDto(User user) {
        UserShortResponseDto userShortResponseDto = new UserShortResponseDto();
        userShortResponseDto.setId(user.getId());
        userShortResponseDto.setName(user.getName());
        userShortResponseDto.setLastName(user.getLastName());
        userShortResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userShortResponseDto;
    }

    public static User mapToUser(UserUpdateRequestDto requestDto, User user) {
        if (!requestDto.getName().isBlank() && !user.getName().equals(requestDto.getName())) {
            user.setName(requestDto.getName());
        }
        if (!requestDto.getLastName().isBlank() && !user.getLastName().equals(requestDto.getLastName())) {
            user.setLastName(requestDto.getLastName());
        }
        if (!requestDto.getPhoneNumber().isBlank() && !user.getPhoneNumber().equals(requestDto.getPhoneNumber())) {
            user.setPhoneNumber(requestDto.getPhoneNumber());
        }
        if (requestDto.getCompanyId() != null && !user.getCompanyId().equals(requestDto.getCompanyId())) {
            user.setCompanyId(requestDto.getCompanyId());
        }

        return user;
    }
}
