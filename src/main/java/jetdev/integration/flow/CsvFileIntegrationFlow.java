package jetdev.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;

import java.io.File;

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
                .handle(m -> log.info("Processing file line: {}", m.getPayload()))
                .get();
    }

}
