package ru.aleksaosk.cloud_staff.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import ru.aleksaosk.cloud_staff.exception.InvalidPhoneNumberException;
import ru.aleksaosk.cloud_staff.exception.PhoneNumberAlreadyInUseException;
import ru.aleksaosk.cloud_staff.service.UserController;
import ru.aleksaosk.cloud_staff.service.dto.UserRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.UserResponseDto;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerMockPostTest extends BaseUserControllerTest {
    @Test
    void addNewUser() throws Exception {
        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(userResponseDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userResponseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(userResponseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(userResponseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void addNewUserWithEmptyName() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("", "lastname", "79889889988", 1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("name cannot be empty")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithNameLength1() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("n", "lastname", "79889889988", 1L);
        UserResponseDto responseDto = new UserResponseDto(1L, requestDto.getName(), requestDto.getLastName(),
                requestDto.getPhoneNumber(), companyDto);
        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(responseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void addNewUserWithNameLength100() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
                "lastname", "79889889988", 1L);
        UserResponseDto responseDto = new UserResponseDto(1L, requestDto.getName(), requestDto.getLastName(),
                requestDto.getPhoneNumber(), companyDto);
        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(responseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void addNewUserWithNameLength101() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
                "lastname", "79889889988", 1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("name cannot exceed 100 characters")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithEmptyLastName() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "", "79889889988", 1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("lastName cannot be empty")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithLastNameLength1() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "l", "79889889988", 1L);
        UserResponseDto responseDto = new UserResponseDto(1L, requestDto.getName(), requestDto.getLastName(),
                requestDto.getPhoneNumber(), companyDto);
        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(responseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void addNewUserWithLastNameLength100() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
                "lastname", "79889889988", 1L);
        UserResponseDto responseDto = new UserResponseDto(1L, requestDto.getName(), requestDto.getLastName(),
                requestDto.getPhoneNumber(), companyDto);
        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(responseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void addNewUserWithLastNameLength101() throws Exception {
        UserRequestDto requestDto = new UserRequestDto(
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
                "lastname", "79889889988", 1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("name cannot exceed 100 characters")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithEmptyPhoneNumber() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName", null, 1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("phoneNumber cannot be empty")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithPhoneNumberLength9() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName", "999888776", 1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("phoneNumber must be between 10 and 35 characters")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithPhoneNumberLength10() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName", "9998887766", 1L);
        UserResponseDto responseDto = new UserResponseDto(1L, requestDto.getName(), requestDto.getLastName(),
                requestDto.getPhoneNumber(), companyDto);
        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(responseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void addNewUserWithPhoneNumberLength35() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName",
                "+ 7 - ( 9 9 9 ) - 9 9 9 - 9 9 - 9 9", 1L);
        UserResponseDto responseDto = new UserResponseDto(1L, requestDto.getName(), requestDto.getLastName(),
                requestDto.getPhoneNumber(), companyDto);
        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.lastName", is(responseDto.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(responseDto.getPhoneNumber())))
                .andExpect(jsonPath("$.company.id", is(companyDto.getId()), Long.class))
                .andExpect(jsonPath("$.company.name", is(companyDto.getName())))
                .andExpect(jsonPath("$.company.budget", is(companyDto.getBudget()), BigDecimal.class));
    }

    @Test
    void addNewUserWithPhoneNumberLength36() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName",
                "+ 7 - ( 9 9 9 ) - 9 9 9 - 9 9 - 9 9 ", 1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("phoneNumber must be between 10 and 35 characters")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithInvalidPhoneNumber() throws Exception {
        doThrow(new InvalidPhoneNumberException("incorrect phone number"))
                .when(userService).addNewUser(any(UserRequestDto.class));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("incorrect phone number")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithUsedPhoneNumber() throws Exception {
        doThrow(new PhoneNumberAlreadyInUseException("phone number is already in use"))
                .when(userService).addNewUser(any(UserRequestDto.class));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is("409 CONFLICT")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("phone number is already in use")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithEmptyCompanyId() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName", "79998887766", null);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("companyId cannot be null")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithCompanyIdNegative() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName", "79998887766", -1L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("companyId must be more than 0")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewUserWithCompanyIdZero() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("name", "lastName", "79998887766", 0L);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("companyId must be more than 0")))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}