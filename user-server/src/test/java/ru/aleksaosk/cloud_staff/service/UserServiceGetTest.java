package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.entity.User;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        Page<User> usersPage = new PageImpl<>(
                List.of(user), PageRequest.of(0, 10), 1);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);
        when(userManager.getCompanyDto(anyLong())).thenReturn(companyDto);

        Page<UserResponseDto> actualUsersDtoPage = userService.getAllUsers(0, 10);
        List<UserResponseDto> actualResponseDtoList = actualUsersDtoPage.getContent();
        assertEquals(userResponseDto.getId(), actualResponseDtoList.get(0).getId());
        assertEquals(userResponseDto.getName(), actualResponseDtoList.get(0).getName());
        assertEquals(userResponseDto.getLastName(), actualResponseDtoList.get(0).getLastName());
        assertEquals(userResponseDto.getPhoneNumber(), actualResponseDtoList.get(0).getPhoneNumber());
        assertEquals(userResponseDto.getCompany(), actualResponseDtoList.get(0).getCompany());
        verify(userRepository).findAll(any(Pageable.class));
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
