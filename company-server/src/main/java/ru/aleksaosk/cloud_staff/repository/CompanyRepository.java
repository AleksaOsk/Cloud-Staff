package ru.aleksaosk.cloud_staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aleksaosk.cloud_staff.entity.Company;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
}
