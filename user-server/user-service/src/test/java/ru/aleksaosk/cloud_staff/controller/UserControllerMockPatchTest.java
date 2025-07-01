package ru.aleksaosk.cloud_staff.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import ru.aleksaosk.cloud_staff.service.UserController;
import ru.aleksaosk.cloud_staff.service.dto.UserUpdateRequestDto;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerMockPatchTest extends BaseUserControllerTest {
    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(eq(user.getId()), any(UserUpdateRequestDto.class)))
                .thenReturn(updateResponseDto);
        mvc.perform(patch("/users/{id}", updateResponseDto.getId())
                        .content(mapper.writeValueAsString(updatedRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateResponseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(updateResponseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(updateResponseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }
}
