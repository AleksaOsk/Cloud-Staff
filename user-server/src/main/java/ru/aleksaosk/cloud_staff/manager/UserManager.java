package ru.aleksaosk.cloud_staff.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserManager {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public UserManager(@Value("${server.url}") String serverUrl, RestTemplate restTemplate) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
    }

    public CompanyDto getCompanyDto(long companyId) {
        log.info("Отправляется запрос на получение company с id = {}", companyId);
        String curUrl = "/companies/" + companyId + "/users";
        return restTemplate.getForObject(serverUrl + curUrl, CompanyDto.class);
    }
}
