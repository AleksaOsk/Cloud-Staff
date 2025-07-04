package ru.aleksaosk.cloud_staff.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class CompanyManager {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public CompanyManager(@Value("${server.url}") String serverUrl, RestTemplate restTemplate) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
    }

    public List<UserDto> getUsersShortDtoList(long companyId) {
        log.info("Отправляется запрос на получение списка users для компании с id = {}", companyId);
        String curUrl = serverUrl + "/users/company/" + companyId;

        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                curUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {
                }
        );
        return response.getBody();
    }


    public void deleteUsersByCompanyId(long companyId) {
        log.info("Отправляется запрос на удаление users у company с id = {}", companyId);
        String curUrl = serverUrl + "/users/company/" + companyId;
        restTemplate.delete(curUrl);
    }
}
