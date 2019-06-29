import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LoadCustomerDetails {
    public Map<String, CustomerProfile> loadCustomersPaymentDetails(File csvFile) throws IOException {
        Map<String, CustomerProfile> customerProfileMap = new HashMap<String, CustomerProfile>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
        String line = "";
        br.readLine();
        int rowcount = 0 ;
        while((line = br.readLine()) != null) {
            String[] row = line.split(",");
            String active = row[0];
            if(active.equalsIgnoreCase("Y")) {
                rowcount++;
                if(row.length < 14)
                    continue;
                System.out.println(rowcount + " : " + row.length);
                String cusId = row[1];
                String apartment = row[2];
                String customerName = row[3];
                String apartmentNo = row[4];
                String carModel = row[5];
                String carNo = row[6];
                String carType = row[7];
                String startDate = row[9];
                String mobile = row[10];
                String email = row[11];
                String price = row[13];
                CustomerCar.CustomerCarBuilder customerCarBuilder = CustomerCar.builder();
                CustomerCar customerCar = customerCarBuilder.carModel(carModel).carNo(carNo).carType(carType)
                        .priceCharge(price).startDate(startDate).build();
                if(!customerProfileMap.containsKey(mobile)) {
                    List<CustomerCar> cars = new LinkedList<CustomerCar>();
                    cars.add(customerCar);
                    CustomerProfile.CustomerProfileBuilder customerProfileBuilder = CustomerProfile.builder();
                    CustomerProfile customerProfile = customerProfileBuilder.apartment(apartment).apartmentNo(apartmentNo).customerId(cusId)
                            .customerName(customerName).email(email).mobile(mobile).cars(cars).build();

                    customerProfileMap.put(mobile, customerProfile);
                } else {
                    CustomerProfile customerProfile = customerProfileMap.get(mobile);
                    List<CustomerCar> cars = customerProfile.getCars();
                    cars.add(customerCar);
                    customerProfile.setCars(cars);
                    customerProfileMap.put(mobile, customerProfile);
                }
            }
        }
        return customerProfileMap;

    }
}
