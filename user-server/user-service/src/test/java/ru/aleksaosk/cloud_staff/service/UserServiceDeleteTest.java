package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceDeleteTest extends BaseUserServiceTest {
    @Test
    void deleteUser() {
        userService.deleteUser(user.getId());
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void deleteUsersByCompanyId() {
        userService.deleteUsersByCompanyId(companyDto.getId());
        verify(userRepository).deleteAllByCompanyId(user.getId());
    }
}
