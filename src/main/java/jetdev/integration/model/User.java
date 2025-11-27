package jetdev.integration.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String firstname;
    private String lastname;
    private String email;
    private String countryCode;
    private String currency;
    private String countryName;
    private Double temperature;
    private Double humidity;
    private String city;
}
