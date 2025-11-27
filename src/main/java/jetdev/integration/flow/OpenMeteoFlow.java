package jetdev.integration.flow;

import jetdev.integration.model.OpenMeteo;
import jetdev.integration.service.OpenMeteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class OpenMeteoFlow {

    private final OpenMeteoService openMeteoService;

    @Bean
    public IntegrationFlow openMeteoFlowProcess() {
        return IntegrationFlow.from("openMeteo")
                .handle(Map.class, (m, headers) ->
                        openMeteoService.fetchTemperatureAndHumidityByLongAndLat((String) m.get("lon"), (String) m.get("lat"))
                )
                .transform(OpenMeteo.class, m ->
                        Map.<String, Object>of(
                                "temperature_2m", m.getHourly().getTemperature().getLast(),
                                "relative_humidity_2m", m.getHourly().getHumidity().getLast()
                        )
                )
                .log(m -> "OpenMeteoFlow received message: " + m.getPayload())
                .channel("aggregateWeatherCountry")
                .get();
    }
}
