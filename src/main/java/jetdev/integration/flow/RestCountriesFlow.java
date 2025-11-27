package jetdev.integration.flow;

import jetdev.integration.model.RestCountries;
import jetdev.integration.service.RestCountriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RestCountriesFlow {

    private final RestCountriesService restCountriesService;

    @Bean
    public IntegrationFlow restCountriesFlowProcess() {
        return IntegrationFlow.from("restCountries")
                .transform(Map.class, m -> m.get("country"))
                .handle(String.class, (countryName, headers) ->
                        restCountriesService.fetchCountryDataByCountryName(countryName)
                )
                .transform(RestCountries.class, m ->
                        Map.of(
                                "country_name", m.getName().getCommon(),
                                "country_code", m.getCountryCode(),
                                "currency", m.getCurrencies().keySet().iterator().next()
                        )
                )
                .log(m -> "RestCountriesFlow received message: " + m.getPayload())
                .channel("aggregateWeatherCountry")
                .get();
    }
}
