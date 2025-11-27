package jetdev.integration.service;

import jetdev.integration.model.OpenMeteo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenMeteoService {
    private final RestTemplate restTemplate;

    @Value("${demo.apis.openmeteo}")
    private String openMeteoApiUrl;

    public Map<String, Double> fetchTemperatureAndHumidityByLongAndLat(String longitude, String latitude) {
        String url = UriComponentsBuilder.fromUriString(openMeteoApiUrl)
                .path("/forecast/")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("hourly", "temperature_2m,relative_humidity_2m")
                .queryParam("format", "json")
                .queryParam("timeformat", "unixtime")
                .toUriString();

        ResponseEntity<@NonNull OpenMeteo> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                OpenMeteo.class
        );
        Map<String, Double> body = new HashMap<>();
        assert response.getBody() != null;
        body.put("temperature", response.getBody().getHourly().getTemperature().getLast());
        body.put("humidity", response.getBody().getHourly().getHumidity().getLast());
        return body;
    }
}
