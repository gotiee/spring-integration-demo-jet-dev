package jetdev.integration.flow;

import jetdev.integration.model.OpenMeteo;
import jetdev.integration.service.OpenMeteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.HashMap;
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
                .transform(OpenMeteo.class, m -> {
                            Map<String, Object> result = new HashMap<>();
                            result.put("temperature", m.getHourly().getTemperature().getLast());
                            result.put("humidity", m.getHourly().getHumidity().getLast());
                            return result;
                        }
                )
                .log(m -> "OpenMeteo response: " + m.getPayload())
                .handle(Map.class, (payload, headers) -> {
                    if((Double) payload.get("temperature") > 10)
                        payload.put("isIceNeeded", true);
                    else
                        payload.put("isIceNeeded", false);
                    return payload;
                })
                .log(m -> "OpenMeteoFlow received message: " + m.getPayload())
                .channel("aggregateWeatherCountry")
                .get();
    }
}
