package ru.aleksaosk.cloud_staff.manager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aleksaosk.cloud_staff.CompanyRestClient;
import ru.aleksaosk.cloud_staff.CompanyShortResponseDto;

@Service
@Slf4j
@AllArgsConstructor
public class UserManager {
    private final CompanyRestClient companyClient;

    public CompanyShortResponseDto getCompanyDto(long companyId) {
        log.info("Отправляется запрос на получение company с id = {}", companyId);
        return companyClient.getCompanyDto(companyId);
    }
}
