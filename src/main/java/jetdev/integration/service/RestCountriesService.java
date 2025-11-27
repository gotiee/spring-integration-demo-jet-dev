package jetdev.integration.service;

import jetdev.integration.model.RestCountries;
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

    public RestCountries fetchCountryDataByCountryName(String country) {
        try {
            String url = UriComponentsBuilder.fromUriString(restcountriesApiUrl)
                    .path("/name/{name}")
                    .buildAndExpand(country)
                    .toUriString();

            ResponseEntity<RestCountries[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    RestCountries[].class
            );

            RestCountries[] results = response.getBody();
            if (results == null || results.length == 0) {
                log.warn("Aucun pays trouvé pour {}", country);
                return new RestCountries();
            }
            return results[0];
        } catch (Exception e) {
            log.error("Erreur lors de l'appel à Rest Countries pour {}: {}", country, e.getMessage());
            return new RestCountries();
        }
    }
}

