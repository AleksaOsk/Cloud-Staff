package ru.aleksaosk.cloud_staff.service.service;

import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.service.dto.CompanyUpdateRequestDto;

import java.util.List;

public interface CompanyService {
    CompanyResponseDto addNewCompany(CompanyRequestDto requestDto);

    CompanyResponseDto updateCompany(Long id, CompanyUpdateRequestDto requestDto);

    CompanyResponseDto getCompany(Long id);

    List<CompanyResponseDto> getAllCompanies();

    void deleteCompany(Long id);

    CompanyShortResponseDto getCompanyById(Long companyId);
}
