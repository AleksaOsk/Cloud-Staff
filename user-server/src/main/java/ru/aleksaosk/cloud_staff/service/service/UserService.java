package ru.aleksaosk.cloud_staff.service.service;

import ru.aleksaosk.cloud_staff.service.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {
    UserResponseDto addNewUser(UserRequestDto requestDto);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto);

    UserResponseDto getUser(Long id);

    List<UserResponseDto> getAllUsers();

    void deleteUser(Long id);

    List<UserShortResponseDto> getUsersByCompanyId(Long componyId);

    void deleteUsersByCompanyId(Long compId);
}
