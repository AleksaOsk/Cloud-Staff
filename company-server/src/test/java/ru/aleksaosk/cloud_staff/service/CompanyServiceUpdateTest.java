package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.entity.Company;
import ru.aleksaosk.cloud_staff.exception.CompanyNameAlreadyInUseException;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceUpdateTest extends BaseCompanyServiceTest {
    @BeforeEach
    void addUsersList() {
        updateResponseDto.setUsers(userShortResponseDtoList);
    }

    @Test
    void updateCompany() {
        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
        when(companyRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(companyManager.getUsersShortDtoList(anyLong())).thenReturn(userShortResponseDtoList);
        when(companyRepository.save(any(Company.class))).thenReturn(updateCompany);
        CompanyResponseDto updatedCompany = companyService.updateCompany(1L, updateRequestDto);
        assertEquals(updateResponseDto.getId(), updatedCompany.getId());
        assertEquals(updateResponseDto.getName(), updatedCompany.getName());
        assertEquals(updateResponseDto.getBudget(), updatedCompany.getBudget());
        assertEquals(updateResponseDto.getUsers(), updatedCompany.getUsers());
        verify(companyRepository).findById(anyLong());
        verify(companyRepository).findByName(any(String.class));
        verify(companyManager).getUsersShortDtoList(anyLong());
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void updateCompanyWhenCompanyNotFound() {
        when(companyRepository.findById(company.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> companyService.updateCompany(company.getId(), updateRequestDto));
        verify(companyRepository).findById(anyLong());
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void updateCompanyWithUsedName() {
        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
        when(companyRepository.findByName(anyString())).thenReturn(Optional.of(company));
        assertThrows(CompanyNameAlreadyInUseException.class,
                () -> companyService.updateCompany(company.getId(), updateRequestDto));
        verify(companyRepository).findById(anyLong());
        verify(companyRepository).findByName(any(String.class));
        verify(companyRepository, never()).save(any(Company.class));
    }
}
