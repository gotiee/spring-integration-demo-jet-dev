package jetdev.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;

import java.io.File;
import java.util.UUID;

@Slf4j
@Configuration
public class CsvFileIntegrationFlow {

    @Bean
    public IntegrationFlow csvFileIngestionFlow() {
        return IntegrationFlow
                .from(
                        Files.inboundAdapter(new File("input"))
                                .patternFilter("*.csv"),
                        c -> c.poller(Pollers.fixedDelay(1000).maxMessagesPerPoll(1))
                )
                .split(Files.splitter())
                .filter(m -> {
                    String line = (String) m;
                    String[] fields = line.split(",");
                    return fields.length >= 3 && fields[2].contains("@");
                })
                .enrichHeaders(h -> h.headerFunction("correlationId", m -> UUID.randomUUID().toString()))
                .handle(m -> {
                    log.info("Correlation ID: {}", m.getHeaders().get("correlationId"));
                    log.info("Processing file line: {}", m.getPayload());
                })
                .get();
    }

}
