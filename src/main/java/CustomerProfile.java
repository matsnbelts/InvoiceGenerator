import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class CustomerProfile {
    private String customerName;
    private String email;
    private String mobile;
    private String customerId;
    private String apartmentNo;
    private String apartment;
    private String pack;
    private List<CustomerCar> cars;
}
