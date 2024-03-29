import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
class CustomerCar {
    private String carNo;
    private String carModel;
    private String carType;
    private String startDate;
    private String endDate;
    private double actualRate;
    private double discountRate;
    private String promoCode;
}
