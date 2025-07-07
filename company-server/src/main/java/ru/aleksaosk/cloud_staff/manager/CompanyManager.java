package ru.aleksaosk.cloud_staff.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class CompanyManager {
    private final RestClient restClient;
    private final String serverUrl;

    public CompanyManager(@Value("${server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
        this.restClient = RestClient.create();
    }

    public List<UserDto> getUsersShortDtoList(long companyId) {
        log.info("Отправляется запрос на получение списка users для компании с id = {}", companyId);
        String curUrl = serverUrl + "/users/company/" + companyId;

        return restClient.get()
                .uri(curUrl)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public void deleteUsersByCompanyId(long companyId) {
        log.info("Отправляется запрос на удаление users у company с id = {}", companyId);
        String curUrl = serverUrl + "/users/company/" + companyId;
        restClient.delete().uri(curUrl);
    }
}
