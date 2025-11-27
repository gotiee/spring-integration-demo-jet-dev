package jetdev.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.store.SimpleMessageStore;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class AggregateUserAndCountryFlow {
    @Bean
    public IntegrationFlow aggregateUserAndCountryFlowProcess() {
        return IntegrationFlow
                .from("aggregateUserAndCountry")
                .aggregate(a -> a
                        .correlationStrategy(m -> m.getHeaders().get("correlationId"))
                        .releaseStrategy(g -> g.size() == 2)
                        .messageStore(new SimpleMessageStore())
                        .outputProcessor(group -> {
                            Map<String, Object> aggregatedPayload = new HashMap<>();
                            group.getMessages().forEach(message -> {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> payload = (Map<String, Object>) message.getPayload();
                                aggregatedPayload.putAll(payload);
                            });
                            log.info("Aggregating group with {}", group.size());
                            return aggregatedPayload;
                        })
                )
                .log(m -> "Aggregated data: " + m.getPayload())
                .channel("nullChannel")
                .get();
    }
}
