package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.manager.CompanyManager;
import ru.aleksaosk.cloud_staff.service.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.entity.Company;
import ru.aleksaosk.cloud_staff.service.service.CompanyServiceImpl;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class BaseCompanyServiceTest {
    @Mock
    protected CompanyRepository companyRepository;
    @Mock
    protected CompanyManager companyManager;
    @InjectMocks
    protected CompanyServiceImpl companyService;

    protected Company company, updateCompany;
    protected CompanyRequestDto companyRequestDto;
    protected CompanyUpdateRequestDto updateRequestDto;
    protected CompanyResponseDto companyResponseDto, updateResponseDto;
    protected List<CompanyResponseDto> companyResponseDtoList;
    protected CompanyShortResponseDto companyShortResponseDto;
    protected UserShortResponseDto userShortResponseDto;
    protected List<UserShortResponseDto> userShortResponseDtoList;

    @BeforeEach
    void setUp() {
        companyRequestDto = new CompanyRequestDto("name", new BigDecimal(10000));
        company = new Company(1L, companyRequestDto.getName(), companyRequestDto.getBudget());
        companyResponseDto = CompanyMapper.mapToCompanyResponseDto(company);
        companyResponseDtoList = List.of(companyResponseDto);

        updateRequestDto = new CompanyUpdateRequestDto("update name", new BigDecimal(50000));
        updateCompany = new Company(1L, updateRequestDto.getName(), updateRequestDto.getBudget());
        updateResponseDto = CompanyMapper.mapToCompanyResponseDto(updateCompany, userShortResponseDtoList);

        companyShortResponseDto = CompanyMapper.mapToCompanyShortResponseDto(company);

        userShortResponseDto = new UserShortResponseDto(1L, "name", "lastname", "79998880077");
        userShortResponseDtoList = List.of(userShortResponseDto);
    }
}
