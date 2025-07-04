package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.exception.CompanyNameAlreadyInUseException;
import ru.aleksaosk.cloud_staff.service.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.service.entity.Company;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceAddTest extends BaseCompanyServiceTest {
    @Test
    void createCompany() {
        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.empty());
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        CompanyResponseDto createdCompany = companyService.addNewCompany(companyRequestDto);
        assertEquals(companyResponseDto.getId(), createdCompany.getId());
        assertEquals(companyResponseDto.getName(), createdCompany.getName());
        assertEquals(companyResponseDto.getBudget(), createdCompany.getBudget());
        verify(companyRepository).findByName(any(String.class));
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void createCompanyWithUsedName() {
        when(companyRepository.findByName(any(String.class))).thenReturn(Optional.of(company));
        assertThrows(CompanyNameAlreadyInUseException.class, () -> companyService.addNewCompany(companyRequestDto));
        verify(companyRepository).findByName(any(String.class));
        verify(companyRepository, never()).save(any(Company.class));
    }
}
