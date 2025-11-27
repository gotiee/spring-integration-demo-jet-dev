package jetdev.integration.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;

import java.util.List;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    private static final List<String> CHANNEL_NAMES = List.of(
            "transformUser",
            "enrichCountry",
            "aggregateUserAndCountry",
            "nominatim"
    );

    @Bean
    public List<DirectChannel> directChannels() {
        return CHANNEL_NAMES.stream()
                .map(this::createDirectChannel)
                .toList();
    }

    private DirectChannel createDirectChannel(String channelName) {
        return new DirectChannel();
    }
}
