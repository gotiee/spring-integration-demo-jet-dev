package jetdev.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Slf4j
@Configuration
public class EnrichCountryFlow {
    @Bean
    public IntegrationFlow enrichCountryFlowProcess() {
        return IntegrationFlow
                .from("enrichCountry")
                .log(m -> "Enriching country data for: " + m.getPayload())
                .channel("nullChannel")
                .get();
    }
}
