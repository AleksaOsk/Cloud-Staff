package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.exception.InvalidPhoneNumberException;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;
import ru.aleksaosk.cloud_staff.exception.PhoneNumberAlreadyInUseException;
import ru.aleksaosk.cloud_staff.service.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.service.entity.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceAddTest extends BaseUserServiceTest {
    @Test
    void createUser() {
        when(userRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.empty());
        when(userManager.getCompanyDto(anyLong())).thenReturn(companyDto);
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserResponseDto createdUser = userService.addNewUser(userRequestDto);
        assertEquals(userResponseDto.getId(), createdUser.getId());
        assertEquals(userResponseDto.getName(), createdUser.getName());
        assertEquals(userResponseDto.getLastName(), createdUser.getLastName());
        assertEquals(userResponseDto.getPhoneNumber(), createdUser.getPhoneNumber());
        assertEquals(userResponseDto.getCompany(), createdUser.getCompany());
        verify(userRepository).findByPhoneNumber(any(String.class));
        verify(userManager).getCompanyDto(anyLong());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUserWithUsedPhoneNumber() {
        when(userRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.of(user));
        assertThrows(PhoneNumberAlreadyInUseException.class, () -> userService.addNewUser(userRequestDto));
        verify(userRepository).findByPhoneNumber(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUserWithInvalidPhoneNumber() {
        UserRequestDto requestDto = new UserRequestDto("name", "lastname", "7999888776", 1L);
        assertThrows(InvalidPhoneNumberException.class, () -> userService.addNewUser(requestDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUserWhenCompanyNotFound() {
        when(userRepository.findByPhoneNumber(any(String.class))).thenReturn(Optional.empty());
        when(userManager.getCompanyDto(anyLong())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> userService.addNewUser(userRequestDto));
        verify(userRepository).findByPhoneNumber(any(String.class));
        verify(userManager).getCompanyDto(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

}
