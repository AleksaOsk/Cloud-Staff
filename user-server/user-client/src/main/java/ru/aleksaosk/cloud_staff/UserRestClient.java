package ru.aleksaosk.cloud_staff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
public class UserRestClient {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    @Autowired
    public UserRestClient(@Value("${server.url}") String serverUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public List<UserShortResponseDto> getUsersShortDtoList(long companyId) {
        log.info("Пришел запрос в UserRestClient для получения списка users для company с id = {}", companyId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl + "/users/company/" + companyId);

        String url = builder.toUriString();
        log.trace("URL: {}", url);

        ResponseEntity<List<UserShortResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserShortResponseDto>>() {
                });
        return response.getBody();
    }

    public void deleteUsersByCompanyId(long companyId) {
        log.info("Пришел запрос в UserRestClient для удаления users у company с id = {}", companyId);
        restTemplate.delete(serverUrl + "/users/company/" + companyId);
    }
}
