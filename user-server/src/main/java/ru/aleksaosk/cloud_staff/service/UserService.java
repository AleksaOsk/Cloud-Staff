package ru.aleksaosk.cloud_staff.service;

import org.springframework.data.domain.Page;
import ru.aleksaosk.cloud_staff.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {
    UserResponseDto addNewUser(UserRequestDto requestDto);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto);

    UserResponseDto getUser(Long id);

    Page<UserResponseDto> getAllUsers(int from, int size);

    void deleteUser(Long id);

    List<UserShortResponseDto> getUsersByCompanyId(Long componyId);

    void deleteUsersByCompanyId(Long compId);
}
