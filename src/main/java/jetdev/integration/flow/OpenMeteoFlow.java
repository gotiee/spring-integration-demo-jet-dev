package jetdev.integration.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class OpenMeteoFlow {
    @Bean
    public IntegrationFlow openMeteoFlowProcess() {
        return IntegrationFlow.from("openMeteo")
                .log(m -> "OpenMeteoFlow received message: " + m.getPayload())
                .channel("nullChannel")
                .get();
    }
}
