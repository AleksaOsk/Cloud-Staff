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

@Slf4j
@Service
public class CompanyRestClient {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    @Autowired
    public CompanyRestClient(@Value("${server.url}") String serverUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public CompanyShortResponseDto getCompanyDto(long compId) {
        log.info("Пришел запрос в CompanyRestClient для получения данных о company с id = {}", compId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl + "/companies/" + compId + "/users");

        String url = builder.toUriString();
        log.trace("URL: {}", url);

        ResponseEntity<CompanyShortResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CompanyShortResponseDto>() {
                });
        return response.getBody();
    }
}
