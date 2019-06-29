import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerCar {
    private String carNo;
    private String carModel;
    private String carType;
    private String startDate;
    private String endDate;
    private String priceCharge;
}
