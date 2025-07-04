package ru.aleksaosk.cloud_staff.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import ru.aleksaosk.cloud_staff.exception.CompanyNameAlreadyInUseException;
import ru.aleksaosk.cloud_staff.service.CompanyController;
import ru.aleksaosk.cloud_staff.service.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CompanyController.class)
public class CompanyControllerMockPostTest extends BaseCompanyControllerTest {
    @Test
    void addNewCompany() throws Exception {
        when(companyService.addNewCompany(any(CompanyRequestDto.class))).thenReturn(companyResponseDto);
        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(companyRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(companyResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(companyResponseDto.getName())))
                .andExpect(jsonPath("$.budget", is(companyResponseDto.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.users").value(nullValue()));
    }

    @Test
    void addNewCompanyWithEmptyName() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto(null, new BigDecimal(12000));

        mvc.perform(post("/companies")
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
    void addNewCompanyWithNameLength3() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto("nnn", new BigDecimal(15000));
        CompanyResponseDto responseDto = new CompanyResponseDto(1L, "nnn", new BigDecimal(15000), null);
        when(companyService.addNewCompany(any(CompanyRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.budget", is(responseDto.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.users").value(nullValue()));
    }

    @Test
    void addNewCompanyWithNameLength250() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto(
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
                new BigDecimal(15000));
        CompanyResponseDto responseDto = new CompanyResponseDto(1L, requestDto.getName(), requestDto.getBudget(), null);
        when(companyService.addNewCompany(any(CompanyRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.budget", is(responseDto.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.users").value(nullValue()));
    }

    @Test
    void addNewCompanyWithNameLength251() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto(
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn",
                new BigDecimal(15000));

        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("name must be between 3 and 250 characters")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewCompanyWhenBudgetNull() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto("name", null);

        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("budget cannot be null")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewCompanyWhenBudgetNegative() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto("name", new BigDecimal(-1));

        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("budget must be positive or zero")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewCompanyWhenBudgetZero() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto("name", new BigDecimal(0));
        CompanyResponseDto responseDto = new CompanyResponseDto(1L, requestDto.getName(), requestDto.getBudget(), null);
        when(companyService.addNewCompany(any(CompanyRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.budget", is(responseDto.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.users").value(nullValue()));
    }

    @Test
    void addNewCompanyWhenBudgetMax() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto("name", new BigDecimal("9999999999999.99"));
        CompanyResponseDto responseDto = new CompanyResponseDto(1L, requestDto.getName(), requestDto.getBudget(), null);
        when(companyService.addNewCompany(any(CompanyRequestDto.class))).thenReturn(responseDto);
        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(responseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(responseDto.getName())))
                .andExpect(jsonPath("$.budget", is(responseDto.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.users").value(nullValue()));
    }

    @Test
    void addNewCompanyWhenBudgetMoreThanMax() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto("name", new BigDecimal("99999999999991.99"));
        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("budget must have up to 13 digits before and 2 after the point")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addNewCompanyWhenBudgetMoreThanMaxAfterPoint() throws Exception {
        CompanyRequestDto requestDto = new CompanyRequestDto("name", new BigDecimal("91.999"));
        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("budget must have up to 13 digits before and 2 after the point")))
                .andExpect(jsonPath("$.timestamp").exists());
    }


    @Test
    void addNewCompanyWhenNameAlreadyUsed() throws Exception {
        doThrow(new CompanyNameAlreadyInUseException("company name is already in use"))
                .when(companyService).addNewCompany(any(CompanyRequestDto.class));

        mvc.perform(post("/companies")
                        .content(mapper.writeValueAsString(companyRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is("409 CONFLICT")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("company name is already in use")))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}