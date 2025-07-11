package ru.aleksaosk.cloud_staff.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import ru.aleksaosk.cloud_staff.dto.UserResponseDto;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerMockGetTest extends BaseUserControllerTest {
    @Test
    void getUser() throws Exception {
        when(userService.getUser(eq(user.getId())))
                .thenReturn(userResponseDto);
        mvc.perform(get("/users/{id}", userResponseDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userResponseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(userResponseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(userResponseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void getUserNotFound() throws Exception {
        doThrow(new NotFoundException("user with id = " + 999 + " not found")).when(userService).getUser(anyLong());

        mvc.perform(get("/users/{id}", 999)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("404 NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("user with id = 999 not found")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getAllUsers() throws Exception {
        Page<UserResponseDto> userResponseDtoPage = new PageImpl<>(
                List.of(userResponseDto), PageRequest.of(0, 10), 1);
        List<UserResponseDto> responseDtoPage = userResponseDtoPage.getContent();

        when(userService.getAllUsers(0, 10))
                .thenReturn(userResponseDtoPage);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(responseDtoPage.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.content[0].name", is(responseDtoPage.get(0).getName())))
                .andExpect(jsonPath("$.content[0].lastName", is(responseDtoPage.get(0).getLastName())))
                .andExpect(jsonPath("$.content[0].phoneNumber", is(responseDtoPage.get(0).getPhoneNumber())))
                .andExpect(jsonPath("$.content[0].company.id", is(responseDtoPage.get(0).getCompany().getId()), Long.class))
                .andExpect(jsonPath("$.content[0].company.name", is(responseDtoPage.get(0).getCompany().getName())))
                .andExpect(jsonPath("$.content[0].company.budget", is(responseDtoPage.get(0).getCompany().getBudget()), BigDecimal.class));
    }

    @Test
    void getUsersByComponyId() throws Exception {
        when(userService.getUsersByCompanyId(anyLong()))
                .thenReturn(userShortResponseDtoList);
        mvc.perform(get("/users/company/{componyId}", companyDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userShortResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userShortResponseDto.getName())))
                .andExpect(jsonPath("$[0].lastName", is(userShortResponseDto.getLastName())))
                .andExpect(jsonPath("$[0].phoneNumber", is(userShortResponseDto.getPhoneNumber())));
    }
}