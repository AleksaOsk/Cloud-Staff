package ru.aleksaosk.cloud_staff.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceDeleteTest extends BaseCompanyServiceTest {
    @Test
    void deleteCompany() {
        companyService.deleteCompany(company.getId());
        verify(companyManager).deleteUsersByCompanyId(company.getId());
        verify(companyRepository).deleteById(company.getId());
    }
}
