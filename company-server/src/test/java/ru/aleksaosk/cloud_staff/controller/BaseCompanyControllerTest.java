package ru.aleksaosk.cloud_staff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.aleksaosk.cloud_staff.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.entity.Company;
import ru.aleksaosk.cloud_staff.manager.UserDto;
import ru.aleksaosk.cloud_staff.mapper.CompanyMapper;
import ru.aleksaosk.cloud_staff.service.CompanyService;

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

    protected final CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);

    protected Company company, updateCompany;
    protected CompanyRequestDto companyRequestDto;
    protected CompanyUpdateRequestDto updateRequestDto;
    protected CompanyResponseDto companyResponseDto, updateResponseDto;
    protected CompanyShortResponseDto companyShortResponseDto;
    protected UserDto userShortResponseDto;
    protected List<UserDto> userShortResponseDtoList;

    @BeforeEach
    void setUp() {
        companyRequestDto = new CompanyRequestDto("name", new BigDecimal(10000));
        company = new Company(1L, companyRequestDto.getName(), companyRequestDto.getBudget());
        companyResponseDto = companyMapper.mapToCompanyResponseDto(company);

        updateRequestDto = new CompanyUpdateRequestDto("update name", new BigDecimal(50000));
        updateCompany = new Company(1L, companyRequestDto.getName(), companyRequestDto.getBudget());
        updateResponseDto = companyMapper.mapToCompanyResponseDto(company);
        updateResponseDto.setUsers(userShortResponseDtoList);

        companyShortResponseDto = companyMapper.mapToCompanyShortResponseDto(company);

        userShortResponseDto = new UserDto(1L, "name", "lastname", "79998880077");
        userShortResponseDtoList = List.of(userShortResponseDto);
    }
}
