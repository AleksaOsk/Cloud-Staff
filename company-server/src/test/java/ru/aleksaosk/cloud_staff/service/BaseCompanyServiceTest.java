package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.entity.Company;
import ru.aleksaosk.cloud_staff.manager.CompanyManager;
import ru.aleksaosk.cloud_staff.manager.UserDto;
import ru.aleksaosk.cloud_staff.mapper.CompanyMapper;
import ru.aleksaosk.cloud_staff.repository.CompanyRepository;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class BaseCompanyServiceTest {
    @Mock
    protected CompanyRepository companyRepository;
    @Mock
    protected CompanyManager companyManager;
    protected final CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);
    protected CompanyServiceImpl companyService;

    protected Company company, updateCompany;
    protected CompanyRequestDto companyRequestDto;
    protected CompanyUpdateRequestDto updateRequestDto;
    protected CompanyResponseDto companyResponseDto, updateResponseDto;
    protected CompanyShortResponseDto companyShortResponseDto;
    protected UserDto userShortResponseDto;
    protected List<UserDto> userShortResponseDtoList;

    @BeforeEach
    void setUp() {
        companyService = new CompanyServiceImpl(companyRepository, companyManager, companyMapper);

        companyRequestDto = new CompanyRequestDto("name", new BigDecimal(10000));
        company = new Company(1L, companyRequestDto.getName(), companyRequestDto.getBudget());
        companyResponseDto = companyMapper.mapToCompanyResponseDto(company);

        updateRequestDto = new CompanyUpdateRequestDto("update name", new BigDecimal(50000));
        updateCompany = new Company(1L, updateRequestDto.getName(), updateRequestDto.getBudget());
        updateResponseDto = companyMapper.mapToCompanyResponseDto(updateCompany);
        updateResponseDto.setUsers(userShortResponseDtoList);

        companyShortResponseDto = companyMapper.mapToCompanyShortResponseDto(company);

        userShortResponseDto = new UserDto(1L, "name", "lastname", "79998880077");
        userShortResponseDtoList = List.of(userShortResponseDto);
    }
}
