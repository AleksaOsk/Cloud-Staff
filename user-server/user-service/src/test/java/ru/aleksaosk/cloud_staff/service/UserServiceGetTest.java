package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.service.entity.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceGetTest extends BaseUserServiceTest {

    @Test
    void getUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userManager.getCompanyDto(anyLong())).thenReturn(companyDto);
        UserResponseDto actualUserDto = userService.getUser(user.getId());
        assertEquals(userResponseDto.getId(), actualUserDto.getId());
        assertEquals(userResponseDto.getName(), actualUserDto.getName());
        assertEquals(userResponseDto.getLastName(), actualUserDto.getLastName());
        assertEquals(userResponseDto.getPhoneNumber(), actualUserDto.getPhoneNumber());
        assertEquals(userResponseDto.getCompany(), actualUserDto.getCompany());
        verify(userRepository).findById(user.getId());
        verify(userManager).getCompanyDto(anyLong());
    }

    @Test
    void getUserWhenUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUser(user.getId()));
        verify(userRepository).findById(user.getId());
    }

    @Test
    void getAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userManager.getCompanyDto(anyLong())).thenReturn(companyDto);
        List<UserResponseDto> actualUserDtoList = userService.getAllUsers();
        assertEquals(userResponseDto.getId(), actualUserDtoList.get(0).getId());
        assertEquals(userResponseDto.getName(), actualUserDtoList.get(0).getName());
        assertEquals(userResponseDto.getLastName(), actualUserDtoList.get(0).getLastName());
        assertEquals(userResponseDto.getPhoneNumber(), actualUserDtoList.get(0).getPhoneNumber());
        assertEquals(userResponseDto.getCompany(), actualUserDtoList.get(0).getCompany());
        verify(userRepository).findAll();
        verify(userManager).getCompanyDto(anyLong());
    }

    @Test
    void getUsersByCompanyId() {
        List<User> users = List.of(user);
        when(userRepository.findAllByCompanyId(anyLong())).thenReturn(users);
        List<UserShortResponseDto> actualUserDtoList = userService.getUsersByCompanyId(companyDto.getId());
        assertEquals(userShortResponseDto.getId(), actualUserDtoList.get(0).getId());
        assertEquals(userShortResponseDto.getName(), actualUserDtoList.get(0).getName());
        assertEquals(userShortResponseDto.getLastName(), actualUserDtoList.get(0).getLastName());
        assertEquals(userShortResponseDto.getPhoneNumber(), actualUserDtoList.get(0).getPhoneNumber());
        verify(userRepository).findAllByCompanyId(anyLong());
    }
}
