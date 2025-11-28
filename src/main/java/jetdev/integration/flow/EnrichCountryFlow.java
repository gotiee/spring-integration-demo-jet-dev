package jetdev.integration.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class EnrichCountryFlow {
    @Bean
    public IntegrationFlow enrichCountryFlowProcess() {
        return IntegrationFlow
                .from("enrichCountry")
                .transform(m -> {
                    String line = (String) m;
                    String[] fields = line.split(",");
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("ville", fields[3].trim());
                    userMap.put("pays", fields[4].trim());
                    return userMap;
                })
                .transform(Map.class, userMap -> {
                    Map<String, String> userCityCountryMap = new HashMap<>();
                    userCityCountryMap.put("city", (String) userMap.get("ville"));
                    userCityCountryMap.put("country", (String) userMap.get("pays"));
                    return userCityCountryMap;
                })
                .log(m -> "Enriched user with country: " + m.getPayload())
                .publishSubscribeChannel(Executors.newCachedThreadPool(), s -> s
                        .subscribe(f -> f.channel("nominatim"))
                        .subscribe(f -> f.channel("restCountries"))
//                        .subscribe(f -> f.gateway(transferCityFlow()))
                )
                .get();
    }
}
