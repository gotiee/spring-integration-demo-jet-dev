package jetdev.integration.service;

import jetdev.integration.model.Nominatim;
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
public class NominatimService {

    private final RestTemplate restTemplate;

    @Value("${demo.apis.nominatim}")
    private String nominatimApiUrl;

    public Nominatim fetchLonLatFromCityAndCountry(String city, String country) {
        String url = UriComponentsBuilder.fromUriString(nominatimApiUrl)
                .path("/search")
                .queryParam("q", city + "+" + country)
                .queryParam("format", "json")
                .toUriString();

        ResponseEntity<Nominatim[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Nominatim[].class
        );

        Nominatim[] results = response.getBody();
        if (results == null || results.length == 0) {
            log.warn("Aucun résultat trouvé pour {} {}", city, country);
            return new Nominatim();
        }

        return results[0];
    }
}
