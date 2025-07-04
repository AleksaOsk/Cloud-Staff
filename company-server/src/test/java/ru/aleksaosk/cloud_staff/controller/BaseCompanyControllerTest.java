package ru.aleksaosk.cloud_staff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.aleksaosk.cloud_staff.manager.UserDto;
import ru.aleksaosk.cloud_staff.service.CompanyController;
import ru.aleksaosk.cloud_staff.service.CompanyMapper;
import ru.aleksaosk.cloud_staff.service.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.entity.Company;
import ru.aleksaosk.cloud_staff.service.service.CompanyService;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CompanyController.class)
public abstract class BaseCompanyControllerTest {
    @MockitoBean
    protected CompanyService companyService;

    protected final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    protected MockMvc mvc;

    protected Company company, updateCompany;
    protected CompanyRequestDto companyRequestDto;
    protected CompanyUpdateRequestDto updateRequestDto;
    protected CompanyResponseDto companyResponseDto, updateResponseDto;
    protected List<CompanyResponseDto> companyResponseDtoList;
    protected CompanyShortResponseDto companyShortResponseDto;
    protected UserDto userShortResponseDto;
    protected List<UserDto> userShortResponseDtoList;

    @BeforeEach
    void setUp() {
        companyRequestDto = new CompanyRequestDto("name", new BigDecimal(10000));
        company = new Company(1L, companyRequestDto.getName(), companyRequestDto.getBudget());
        companyResponseDto = CompanyMapper.mapToCompanyResponseDto(company);
        companyResponseDtoList = List.of(companyResponseDto);

        updateRequestDto = new CompanyUpdateRequestDto("update name", new BigDecimal(50000));
        updateCompany = new Company(1L, companyRequestDto.getName(), companyRequestDto.getBudget());
        updateResponseDto = CompanyMapper.mapToCompanyResponseDto(company, userShortResponseDtoList);

        companyShortResponseDto = CompanyMapper.mapToCompanyShortResponseDto(company);

        userShortResponseDto = new UserDto(1L, "name", "lastname", "79998880077");
        userShortResponseDtoList = List.of(userShortResponseDto);
    }
}
