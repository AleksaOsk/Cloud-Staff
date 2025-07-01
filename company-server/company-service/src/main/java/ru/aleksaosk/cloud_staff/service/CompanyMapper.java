package ru.aleksaosk.cloud_staff.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.UserShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyUpdateRequestDto;
import ru.aleksaosk.cloud_staff.service.entity.Company;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompanyMapper {
    public static Company mapToCompany(CompanyRequestDto requestDto) {
        Company company = new Company();
        company.setName(requestDto.getName());
        company.setBudget(requestDto.getBudget());
        return company;
    }

    public static CompanyResponseDto mapToCompanyResponseDto(Company company) {
        CompanyResponseDto companyResponseDto = new CompanyResponseDto();
        companyResponseDto.setId(company.getId());
        companyResponseDto.setName(company.getName());
        companyResponseDto.setBudget(company.getBudget());
        return companyResponseDto;
    }

    public static CompanyResponseDto mapToCompanyResponseDto(Company company, List<UserShortResponseDto> list) {
        CompanyResponseDto companyResponseDto = mapToCompanyResponseDto(company);
        companyResponseDto.setUsers(list);
        return companyResponseDto;
    }

    public static CompanyShortResponseDto mapToCompanyShortResponseDto(Company company) {
        CompanyShortResponseDto companyShortResponseDto = new CompanyShortResponseDto();
        companyShortResponseDto.setId(company.getId());
        companyShortResponseDto.setName(company.getName());
        companyShortResponseDto.setBudget(company.getBudget());
        return companyShortResponseDto;
    }

    public static Company mapToCompany(Company company, CompanyUpdateRequestDto requestDto) {
        if (!requestDto.getName().isBlank() && !company.getName().equals(requestDto.getName())) {
            company.setName(requestDto.getName());
        }
        if (requestDto.getBudget() != null && !company.getBudget().equals(requestDto.getBudget())) {
            company.setBudget(requestDto.getBudget());
        }
        return company;
    }
}
