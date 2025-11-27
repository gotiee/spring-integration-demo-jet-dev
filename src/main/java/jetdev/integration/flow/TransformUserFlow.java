package jetdev.integration.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
public class TransformUserFlow {
    @Bean
    public IntegrationFlow transformUserFlowProcess() {
        return IntegrationFlow
                .from("transformUser")
                .log(m -> "Transforming user: " + m.getPayload())
                .channel("nullChannel")
                .get();
    }
}
