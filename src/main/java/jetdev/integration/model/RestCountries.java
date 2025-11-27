package jetdev.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestCountries {
    @JsonProperty("name")
    private Name name;

    @JsonProperty("cca2")
    private String countryCode;

    @JsonProperty("currencies")
    private Map<String, Currency> currencies;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Name {
        @JsonProperty("common")
        private String common;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Currency {
        @JsonProperty("name")
        private String name;
    }
}
