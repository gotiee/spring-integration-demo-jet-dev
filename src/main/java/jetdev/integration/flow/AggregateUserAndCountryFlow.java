package jetdev.integration.flow;

import jetdev.integration.model.User;
import jetdev.integration.service.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.store.SimpleMessageStore;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AggregateUserAndCountryFlow {

    private static final String OUTPUT_TOPIC = "user"; // Topic Kafka
    private final MessageProducer messageProducer;

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
                .handle(Map.class, (payload, headers) -> {
                    String correlationId = (String) headers.get("correlationId");
                    if (correlationId == null) {
                        throw new IllegalArgumentException("correlationId is required");
                    }
                    User user = User.builder()
                            .firstname(payload.get("firstname").toString())
                            .lastname(payload.get("lastname").toString())
                            .email(payload.get("email").toString())
                            .countryCode(payload.get("country_code").toString())
                            .currency(payload.get("currency").toString())
                            .countryName(payload.get("country_name").toString())
                            .temperature(Double.parseDouble(payload.get("temperature").toString()))
                            .humidity(Double.parseDouble(payload.get("humidity").toString()))
                            .city(payload.get("city").toString())
                            .build();
                    log.info("Sending to Kafka topic {} for correlationId: {}", OUTPUT_TOPIC, correlationId);
                    messageProducer.sendMessage(OUTPUT_TOPIC, user);
                    return null;
                })
                .get();
    }
}
