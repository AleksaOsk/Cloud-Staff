package ru.aleksaosk.cloud_staff.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import ru.aleksaosk.cloud_staff.service.CompanyController;
import ru.aleksaosk.cloud_staff.service.dto.CompanyUpdateRequestDto;

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
@WebMvcTest(CompanyController.class)
public class CompanyControllerMockPatchTest extends BaseCompanyControllerTest {
    @Test
    void updateUser() throws Exception {
        updateResponseDto.setUsers(userShortResponseDtoList);
        when(companyService.updateCompany(eq(company.getId()), any(CompanyUpdateRequestDto.class)))
                .thenReturn(updateResponseDto);
        mvc.perform(patch("/companies/{id}", updateResponseDto.getId())
                        .content(mapper.writeValueAsString(updateRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateResponseDto.getName())))
                .andExpect(jsonPath("$.budget", is(updateResponseDto.getBudget()), BigDecimal.class))
                .andExpect(jsonPath("$.users[0].id", is(updateResponseDto.getUsers().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.users[0].name", is(updateResponseDto.getUsers().get(0).getName())))
                .andExpect(jsonPath("$.users[0].lastName", is(updateResponseDto.getUsers().get(0).getLastName())))
                .andExpect(jsonPath("$.users[0].phoneNumber", is(updateResponseDto.getUsers().get(0).getPhoneNumber())));
    }
}
