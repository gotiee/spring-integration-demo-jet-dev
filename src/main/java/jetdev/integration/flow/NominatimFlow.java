package jetdev.integration.flow;

import jetdev.integration.model.Nominatim;
import jetdev.integration.service.NominatimService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class NominatimFlow {
    private final NominatimService nominatimService;

    @Bean
    public IntegrationFlow nominatimFlowProcess() {
        return IntegrationFlow
                .from("nominatim")
                .handle(Map.class, (m, headers) ->
                        nominatimService.fetchLonLatFromCityAndCountry((String) m.get("city"), (String) m.get("country"))
                )
                .transform(Nominatim.class, n -> Map.of(
                        "lat", n.getLat(),
                        "lon", n.getLon(),
                        "city", n.getDisplayName()
                ))
                .log(m -> "Nominatim response: " + m.getPayload())
                .publishSubscribeChannel(Executors.newCachedThreadPool(), s -> s
                        .subscribe(f -> f.channel("openMeteo"))
                        .subscribe(f -> f.gateway(transferCityFlow()))
                )
                .get();
    }

    @Bean
    public IntegrationFlow transferCityFlow() {
        return f -> f
                .transform(Map.class, nominatimMap -> {
                    Map<String, String> cityMap = new HashMap<>();
                    cityMap.put("city", (String) nominatimMap.get("city"));
                    return cityMap;
                })
                .log(m -> "Transferring city data: " + m.getPayload())
                .channel("aggregateWeatherCountry");
    }
}
