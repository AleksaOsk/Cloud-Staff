package ru.aleksaosk.cloud_staff.service;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.aleksaosk.cloud_staff.service.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByCompanyId(long companyId);

    @Modifying
    @Query(value = "DELETE FROM users WHERE company_id = ?1", nativeQuery = true)
    @Transactional
    void deleteAllByCompanyId(long companyId);

    Optional<User> findByPhoneNumber(String phoneNumber);
}
