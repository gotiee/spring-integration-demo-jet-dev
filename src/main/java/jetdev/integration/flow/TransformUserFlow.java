package jetdev.integration.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransformUserFlow {
    @Bean
    public IntegrationFlow transformUserFlowProcess() {
        return IntegrationFlow
                .from("transformUser")
                .transform(m -> {
                    String line = (String) m;
                    String[] fields = line.split(",");
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("lastname", fields[0].trim());
                    userMap.put("firstname", fields[1].trim());
                    userMap.put("email", fields[2].trim());
                    return userMap;
                })
                .log(m -> "Transformed user: " + m.getPayload())
                .channel("aggregateUserAndCountry")
                .get();
    }
}
