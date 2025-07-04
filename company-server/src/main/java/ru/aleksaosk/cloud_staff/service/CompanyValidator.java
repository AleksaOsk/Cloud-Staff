package ru.aleksaosk.cloud_staff.service;

import ru.aleksaosk.cloud_staff.exception.CompanyNameAlreadyInUseException;
import ru.aleksaosk.cloud_staff.service.entity.Company;

import java.util.Optional;

public class CompanyValidator {
    public static void checkCompanyName(Optional<Company> compOpt) {
        if (compOpt.isPresent()) {
            throw new CompanyNameAlreadyInUseException("company name is already in use");
        }
    }
}
