package ru.aleksaosk.cloud_staff.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class UserManager {
    private final RestClient restClient;
    private final String serverUrl;

    public UserManager(@Value("${server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
        this.restClient = RestClient.create();
    }

    public CompanyDto getCompanyDto(long companyId) {
        log.info("Отправляется запрос на получение company с id = {}", companyId);
        String curUrl = serverUrl + "/companies/" + companyId + "/users";
        return restClient
                .get()
                .uri(curUrl)
                .retrieve()
                .toEntity(CompanyDto.class)
                .getBody();
    }
}
