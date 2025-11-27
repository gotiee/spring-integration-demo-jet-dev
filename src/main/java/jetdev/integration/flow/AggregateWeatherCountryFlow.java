package jetdev.integration.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class AggregateWeatherCountryFlow {
    @Bean
    public IntegrationFlow aggregateWeatherCountryFlowProcess() {
        return IntegrationFlow.from("aggregateWeatherCountry")
                .log(m -> "Aggregating weather and country data: " + m.getPayload())
                .channel("aggregateUserAndCountry")
                .get();
    }
}
