package jetdev.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteo {

    @JsonProperty("hourly")
    private HourlyData hourly;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HourlyData {
        @JsonProperty("time")
        private List<Long> time;

        @JsonProperty("temperature_2m")
        private List<Double> temperature;

        @JsonProperty("relative_humidity_2m")
        private List<Double> humidity;
    }
}
