package ru.aleksaosk.cloud_staff.service;

import org.springframework.data.domain.Page;
import ru.aleksaosk.cloud_staff.dto.CompanyRequestDto;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyUpdateRequestDto;

public interface CompanyService {
    CompanyResponseDto addNewCompany(CompanyRequestDto requestDto);

    CompanyResponseDto updateCompany(Long id, CompanyUpdateRequestDto requestDto);

    CompanyResponseDto getCompany(Long id);

    Page<CompanyResponseDto> getAllCompanies(int from, int size);

    void deleteCompany(Long id);

    CompanyShortResponseDto getCompanyById(Long companyId);
}
