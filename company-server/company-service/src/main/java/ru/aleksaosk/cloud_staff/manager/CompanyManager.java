package ru.aleksaosk.cloud_staff.manager;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.aleksaosk.cloud_staff.UserRestClient;
import ru.aleksaosk.cloud_staff.UserShortResponseDto;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor

public class CompanyManager {
    private final UserRestClient userClient;

    public List<UserShortResponseDto> getUsersShortDtoList(long companyId) {
        log.info("Отправляется запрос на получение списка users для компании с id = {}", companyId);
        return userClient.getUsersShortDtoList(companyId);
    }

    public void deleteUsersByCompanyId(long companyId) {
        log.info("Отправляется запрос на удаление users у company с id = {}", companyId);
        userClient.deleteUsersByCompanyId(companyId);
    }
}
