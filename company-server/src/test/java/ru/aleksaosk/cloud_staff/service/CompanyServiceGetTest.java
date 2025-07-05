package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.aleksaosk.cloud_staff.dto.CompanyResponseDto;
import ru.aleksaosk.cloud_staff.dto.CompanyShortResponseDto;
import ru.aleksaosk.cloud_staff.entity.Company;
import ru.aleksaosk.cloud_staff.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceGetTest extends BaseCompanyServiceTest {
    @BeforeEach
    void addUsersToCompany() {
        companyResponseDto.setUsers(userShortResponseDtoList);
    }

    @Test
    void getCompany() {
        when(companyRepository.findById(company.getId())).thenReturn(Optional.of(company));
        when(companyManager.getUsersShortDtoList(anyLong())).thenReturn(userShortResponseDtoList);
        CompanyResponseDto actualUserDto = companyService.getCompany(company.getId());
        assertEquals(companyResponseDto.getId(), actualUserDto.getId());
        assertEquals(companyResponseDto.getName(), actualUserDto.getName());
        assertEquals(companyResponseDto.getBudget(), actualUserDto.getBudget());
        assertEquals(companyResponseDto.getUsers(), actualUserDto.getUsers());
        verify(companyRepository).findById(company.getId());
        verify(companyManager).getUsersShortDtoList(anyLong());
    }

    @Test
    void getCompanyWhenCompanyNotFound() {
        when(companyRepository.findById(company.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> companyService.getCompany(company.getId()));
        verify(companyRepository).findById(company.getId());
    }

    @Test
    void getAllCompany() {
        Page<Company> companiesPage = new PageImpl<>(
                List.of(company), PageRequest.of(0, 10), 1);
        when(companyRepository.findAll(any(Pageable.class))).thenReturn(companiesPage);
        when(companyManager.getUsersShortDtoList(anyLong())).thenReturn(userShortResponseDtoList);

        Page<CompanyResponseDto> actualCompanyDtoPage = companyService.getAllCompanies(0, 10);
        List<CompanyResponseDto> actualResponseDtoList = actualCompanyDtoPage.getContent();
        assertEquals(companyResponseDto.getId(), actualResponseDtoList.get(0).getId());
        assertEquals(companyResponseDto.getName(), actualResponseDtoList.get(0).getName());
        assertEquals(companyResponseDto.getBudget(), actualResponseDtoList.get(0).getBudget());
        assertEquals(companyResponseDto.getUsers(), actualResponseDtoList.get(0).getUsers());
        verify(companyRepository).findAll(any(Pageable.class));
        verify(companyManager).getUsersShortDtoList(anyLong());
    }

    @Test
    void getCompanyForUserService() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
        CompanyShortResponseDto actualCompany = companyService.getCompanyById(company.getId());
        assertEquals(companyShortResponseDto.getId(), actualCompany.getId());
        assertEquals(companyShortResponseDto.getName(), actualCompany.getName());
        assertEquals(companyShortResponseDto.getBudget(), actualCompany.getBudget());
        verify(companyRepository).findById(anyLong());
    }
}
