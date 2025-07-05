package ru.aleksaosk.cloud_staff.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
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
@WebMvcTest(CompanyController.class)
public class CompanyControllerMockGetTest extends BaseCompanyControllerTest {
    @BeforeEach
    void addUsersToCompany() {
        companyResponseDto.setUsers(userShortResponseDtoList);
    }

    @Test
    void getCompany() throws Exception {
        when(companyService.getCompany(eq(company.getId())))
                .thenReturn(companyResponseDto);
        mvc.perform(get("/companies/{id}", companyResponseDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(companyResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(companyResponseDto.getName())))
                .andExpect(jsonPath("$.budget", is(companyResponseDto.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.users[0].id", is(companyResponseDto.getUsers().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.users[0].name", is(companyResponseDto.getUsers().get(0).getName())))
                .andExpect(jsonPath("$.users[0].lastName", is(companyResponseDto.getUsers().get(0).getLastName())))
                .andExpect(jsonPath("$.users[0].phoneNumber", is(companyResponseDto.getUsers().get(0).getPhoneNumber())));
    }

    @Test
    void getCompanyNotFound() throws Exception {
        doThrow(new NotFoundException("company with id = " + 999 + " not found"))
                .when(companyService).getCompany(anyLong());

        mvc.perform(get("/companies/{id}", 999)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("404 NOT_FOUND")))
                .andExpect(jsonPath("$.reason", is("incorrect data")))
                .andExpect(jsonPath("$.message", is("company with id = 999 not found")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getAllCompany() throws Exception {
        Page<CompanyResponseDto> companyResponseDtoPage = new PageImpl<>(
                List.of(companyResponseDto), PageRequest.of(0, 10), 1);
        List<CompanyResponseDto> companyResponseDtoList = companyResponseDtoPage.getContent();

        when(companyService.getAllCompanies(0, 10))
                .thenReturn(companyResponseDtoPage);
        mvc.perform(get("/companies")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(companyResponseDtoList.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.content[0].name", is(companyResponseDtoList.get(0).getName())))
                .andExpect(jsonPath("$.content[0].budget", is(companyResponseDtoList.get(0).getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.content[0].users[0].id", is(companyResponseDtoList.get(0).getUsers().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.content[0].users[0].name", is(companyResponseDtoList.get(0).getUsers().get(0).getName())))
                .andExpect(jsonPath("$.content[0].users[0].lastName", is(companyResponseDtoList.get(0).getUsers().get(0).getLastName())))
                .andExpect(jsonPath("$.content[0].users[0].phoneNumber", is(companyResponseDtoList.get(0).getUsers().get(0).getPhoneNumber())));
    }

    @Test
    void getCompanyByIdForUserService() throws Exception {
        when(companyService.getCompanyById(anyLong()))
                .thenReturn(companyShortResponseDto);
        mvc.perform(get("/companies/{componyId}/users", company.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(companyShortResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(companyShortResponseDto.getName())))
                .andExpect(jsonPath("$.budget", is(companyShortResponseDto.getBudget()), BigDecimal.class));
    }
}