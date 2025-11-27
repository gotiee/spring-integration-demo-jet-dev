package jetdev.integration.service;

import jetdev.integration.model.RestCountries;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestCountriesService {
    private final RestTemplate restTemplate;

    @Value("${demo.apis.restcountries}")
    private String restcountriesApiUrl;

    public RestCountries fetch(String country) {
        String url = UriComponentsBuilder.fromUriString(restcountriesApiUrl)
                .path("/name/" + country)
                .toUriString();

        ResponseEntity<@NonNull RestCountries> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                RestCountries.class
        );
        return response.getBody();
    }
}
