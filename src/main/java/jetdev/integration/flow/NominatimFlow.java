package jetdev.integration.flow;

import jetdev.integration.service.NominatimService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.Map;

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
                .log(m -> "Nominatim response: " + m.getPayload())
                .channel("nullChannel")
                .get();
    }
}
