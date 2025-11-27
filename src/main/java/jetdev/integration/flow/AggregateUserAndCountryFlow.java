package jetdev.integration.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class AggregateUserAndCountryFlow {
    @Bean
    public IntegrationFlow aggregateUserAndCountryFlowProcess() {
        return IntegrationFlow
                .from("aggregateUserAndCountry")
                .log(m -> "Aggregating user and country data: " + m.getPayload())
                .channel("nullChannel")
                .get();
    }
}
