package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.dto.UserUpdateRequestDto;
import ru.aleksaosk.cloud_staff.entity.User;
import ru.aleksaosk.cloud_staff.exception.InvalidPhoneNumberException;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;
import ru.aleksaosk.cloud_staff.exception.PhoneNumberAlreadyInUseException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUpdateTest extends BaseUserServiceTest {
    @Test
    void updateUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(userManager.getCompanyDto(anyLong())).thenReturn(companyDto);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        UserResponseDto updatedUser = userService.updateUser(1L, updatedRequest);
        assertEquals(updateResponseDto.getId(), updatedUser.getId());
        assertEquals(updateResponseDto.getName(), updatedUser.getName());
        assertEquals(updateResponseDto.getLastName(), updatedUser.getLastName());
        assertEquals(updateResponseDto.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(updateResponseDto.getCompany(), updatedUser.getCompany());
        verify(userRepository).findById(anyLong());
        verify(userRepository).findByPhoneNumber(any(String.class));
        verify(userManager).getCompanyDto(anyLong());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserWhenUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateUser(user.getId(), updatedRequest));
        verify(userRepository).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserWithUsedPhoneNumber() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        assertThrows(PhoneNumberAlreadyInUseException.class, () -> userService.updateUser(user.getId(), updatedRequest));
        verify(userRepository).findById(anyLong());
        verify(userRepository).findByPhoneNumber(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserWithInvalidPhoneNumber() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("name", "lastname", "7999888776", 1L);
        assertThrows(InvalidPhoneNumberException.class, () -> userService.updateUser(user.getId(), requestDto));
        verify(userRepository).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserWhenCompanyNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(userManager.getCompanyDto(anyLong())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> userService.updateUser(user.getId(), updatedRequest));
        verify(userRepository).findById(anyLong());
        verify(userRepository).findByPhoneNumber(any(String.class));
        verify(userManager).getCompanyDto(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }
}
