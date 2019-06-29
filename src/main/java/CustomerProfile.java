import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CustomerProfile {
    private String customerName;
    private String email;
    private String mobile;
    private String customerId;
    private String apartmentNo;
    private String apartment;
    private List<CustomerCar> cars;
}
