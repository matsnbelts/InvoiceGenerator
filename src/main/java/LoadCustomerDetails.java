import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoadCustomerDetails {
    private static class PromoCriteria {
        static final String PROMO100  = "PromoHundred";
        static final String PROMO200 = "Promo200";
        static final String PROMO20= "Promo20%";
        static final String PROMO_FREE = "PromoFree";
    }

    private static class DayCriteria {
        static final int DAYS_MORE_THAN_20 = 20;
        static final int DAYS_MORE_THAN_8 = 8;
        static final int DAYS_MORE_THAN_3 = 3;
    }

    private static class Pack {
        static final String FULL = "Full";
        static final String MINI = "Mini";
        static final String BIKE = "Bike";
    }
    public static class CarType {
        static final String HATCHBACK = "Hatchback";
        static final String SMALL_CAR = "Small Car";
        static final String SEDAN = "Sedan";
        static final String SUV = "SUV";
        static final String BIKE = "Bike";
    }

    private static final double BIKE_PRICE = 300;
    private Map<Integer, Map<String, Map<String, Double>>> priceMap;

    private double applyPromocode(double actualRate, String promoCode) {
        double discountRate;
        if(promoCode.equalsIgnoreCase(PromoCriteria.PROMO100)) {
            discountRate = actualRate - 100;
        } else if(promoCode.equalsIgnoreCase(PromoCriteria.PROMO200)) {
            discountRate = actualRate - 200;
        } else if(promoCode.equalsIgnoreCase(PromoCriteria.PROMO20)) {
            discountRate = actualRate - (actualRate * .2);
        } else if(promoCode.equalsIgnoreCase(PromoCriteria.PROMO_FREE)) {
            discountRate = 0;
        } else {
            discountRate = actualRate;
        }
        return discountRate;
    }
    public LoadCustomerDetails() {
        priceMap = new LinkedHashMap<Integer, Map<String, Map<String, Double>>>();

        Map<String, Double> carMap = new LinkedHashMap<String, Double>();
        carMap.put(CarType.HATCHBACK, 500.00);
        carMap.put(CarType.SMALL_CAR, 500.00);
        carMap.put(CarType.SEDAN, 600.00);
        carMap.put(CarType.SUV, 700.00);
        Map<String, Map<String, Double>> packMap = new LinkedHashMap<String, Map<String, Double>>();
        packMap.put(Pack.FULL, carMap);
        carMap = new LinkedHashMap<String, Double>();
        carMap.put(CarType.HATCHBACK, 300.00);
        carMap.put(CarType.SMALL_CAR, 300.00);
        carMap.put(CarType.SEDAN, 350.00);
        carMap.put(CarType.SUV, 400.00);
        packMap.put(Pack.MINI, carMap);
        priceMap.put(DayCriteria.DAYS_MORE_THAN_20, packMap);

        //--------
        carMap = new LinkedHashMap<String, Double>();
        carMap.put(CarType.HATCHBACK, 300.00);
        carMap.put(CarType.SMALL_CAR, 300.00);
        carMap.put(CarType.SEDAN, 350.00);
        carMap.put(CarType.SUV, 400.00);
        packMap = new LinkedHashMap<String, Map<String, Double>>();
        packMap.put(Pack.FULL, carMap);
        carMap = new LinkedHashMap<String, Double>();
        carMap.put(CarType.HATCHBACK, 250.00);
        carMap.put(CarType.SMALL_CAR, 250.00);
        carMap.put(CarType.SEDAN, 300.00);
        carMap.put(CarType.SUV, 350.00);
        packMap.put(Pack.MINI, carMap);
        priceMap.put(DayCriteria.DAYS_MORE_THAN_8, packMap);

        //--------
        carMap = new LinkedHashMap<String, Double>();
        carMap.put(CarType.HATCHBACK, 150.00);
        carMap.put(CarType.SMALL_CAR, 150.00);
        carMap.put(CarType.SEDAN, 200.00);
        carMap.put(CarType.SUV, 250.00);
        packMap = new LinkedHashMap<String, Map<String, Double>>();
        packMap.put(Pack.FULL, carMap);
        carMap = new LinkedHashMap<String, Double>();
        carMap.put(CarType.HATCHBACK, 100.00);
        carMap.put(CarType.SMALL_CAR, 100.00);
        carMap.put(CarType.SEDAN, 125.00);
        carMap.put(CarType.SUV, 150.00);
        packMap.put(Pack.MINI, carMap);
        priceMap.put(DayCriteria.DAYS_MORE_THAN_3, packMap);

        //---------------
    }

    private double getCarRate(final String pack, final String startDate, final String carType) throws ParseException {

        Date date = new SimpleDateFormat("dd-MMM-yy").parse(startDate);
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(new Date());

        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int validDays;
        if (date.before(new SimpleDateFormat("dd-MMM-yy").parse("01-Jul-19"))
        ) {
            validDays = 30;
        }
        else if (date.after(new SimpleDateFormat("dd-MMM-yy").parse("31-Jul-19"))
        ) {
            validDays = 0;

        } else {
            int totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            validDays = totalDays - day + 1;
            System.out.println(totalDays + " : " + day + " : " + validDays);
        }

        double rate = 0;
        if(pack.equalsIgnoreCase(Pack.BIKE)) {
            if (day >= 16) {
                rate = BIKE_PRICE / 2;
            } else {
                rate = BIKE_PRICE;
            }
        }
        else if(validDays >= DayCriteria.DAYS_MORE_THAN_20) {
            rate = priceMap.get(DayCriteria.DAYS_MORE_THAN_20).get(pack).get(carType);
        } else if(validDays >= DayCriteria.DAYS_MORE_THAN_8) {
            rate = priceMap.get(DayCriteria.DAYS_MORE_THAN_8).get(pack).get(carType);
        } else if(validDays >= DayCriteria.DAYS_MORE_THAN_3) {
            rate = priceMap.get(DayCriteria.DAYS_MORE_THAN_3).get(pack).get(carType);
        }
        return rate;
    }

    Map<String, CustomerProfile> loadCustomersPaymentDetails(File csvFile) throws IOException, ParseException {
        Map<String, CustomerProfile> customerProfileMap = new HashMap<String, CustomerProfile>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
        String line = "";
        //br.readLine();
        int rowcount = 0 ;
        while((line = br.readLine()) != null) {
            System.out.println(line);
            String[] row = line.split(",");
            if(row.length<1) continue;
            String active = row[0];
            System.out.println(active + ":" + active.equalsIgnoreCase("Y"));
            if(active.equalsIgnoreCase("Y")) {
                rowcount++;
//                if(row.length < 14)
//                    continue;
                String cusId = row[1];
                String pack = row[2];
                System.out.println(rowcount + " : " + row.length + " : " + cusId);
                String apartment = row[3];
                String customerName = row[4];
                String apartmentNo = row[5];
                if(row[6] == null || row[6].isEmpty()) {
                    System.out.println("No Car model found for " + cusId);
                    continue;
                }
                String carModel = row[6];
                String carNo = row[7];
                if(row[8] == null || row[8].isEmpty()) {
                    System.out.println("No Car Type found for " + cusId);
                    continue;
                }
                String carType = row[8];
                String startDate = row[9];
                if (new SimpleDateFormat("dd-MMM-yy").parse(startDate).after(new SimpleDateFormat("dd-MMM-yy").parse("31-Jul-19"))
                ) {
                    System.out.println("saaaaaaa " + startDate + " : " + cusId);
                    continue;
                } else if (new SimpleDateFormat("dd-MMM-yy").parse(startDate).before(new SimpleDateFormat("dd-MMM-yy").parse("01-Jul-19"))
                ) {
                    startDate = "01-Jul-19";
                }
                String mobile = (row.length > 10) ? row[10] : "";
//
//                if(mobile.isEmpty()) {
//                    System.out.println("No mobile found for " + cusId);
//                    continue;
//                }
                String email = (row.length > 11) ? row[11] : "";
                String promo = (row.length > 13) ? row[13] : "";

                CustomerCar.CustomerCarBuilder customerCarBuilder = CustomerCar.builder();
                carType = (carType.contains(CarType.SUV)) ? CarType.SUV : carType;
                final double actualRate = getCarRate(pack, startDate, carType);
                CustomerCar customerCar = customerCarBuilder.carModel(carModel).carNo(carNo).carType(carType)
                        .actualRate(actualRate).discountRate(applyPromocode(actualRate, promo)).promoCode(promo).startDate(startDate).build();
                CustomerProfile customerProfile;
                if(!customerProfileMap.containsKey(cusId)) {
                    List<CustomerCar> cars = new LinkedList<CustomerCar>();
                    cars.add(customerCar);
                    CustomerProfile.CustomerProfileBuilder customerProfileBuilder = CustomerProfile.builder();
                    customerProfile = customerProfileBuilder.apartment(apartment).apartmentNo(apartmentNo).customerId(cusId)
                            .customerName(customerName).email(email).mobile(mobile).cars(cars).build();

                    customerProfileMap.put(cusId, customerProfile);
                    if(cusId.equalsIgnoreCase("VS-42")) {
                        System.out.println("----- " + cusId);
                        System.out.println(customerProfileMap.get(cusId));
                    }
                } else {
                    customerProfile = customerProfileMap.get(cusId);
                    List<CustomerCar> cars = customerProfile.getCars();
                    cars.add(customerCar);
                    customerProfile.setCars(cars);
                    customerProfileMap.put(cusId, customerProfile);
                }
                //System.out.println(customerProfile);
            }
        }
        return customerProfileMap;

    }
}
