import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class InvoiceGeneratorApp {
    private static final String CSV_FILE = "/Users/srinis/Documents/MatsNBelts/CustomerDetailsJune2019.csv";
    private static final String OUTPUT_FOLDER = "/Users/srinis/Documents/MatsNBelts/test-invoice/";
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(String.format("%.02f", 600f));
        LoadCustomerDetails loadCustomerDetails = new LoadCustomerDetails();
        Map<String, CustomerProfile> customerProfileMap = loadCustomerDetails
                .loadCustomersPaymentDetails(new File(CSV_FILE));
        for(Map.Entry<String, CustomerProfile> customerProfileEntry: customerProfileMap.entrySet()) {
            InvoiceGenerator.InvoiceGeneratorBuilder invoiceGeneratorBuilder = InvoiceGenerator.builder();
            InvoiceGenerator invoiceGenerator = invoiceGeneratorBuilder.customerProfile(customerProfileEntry.getValue())
                    .area("Karapakkam").cityPincode("Chennai - 600097")
                    .destinationFile(OUTPUT_FOLDER + customerProfileEntry.getValue().getCustomerId()+ ".pdf").build();
            invoiceGenerator.generateInvoicePdf();
        }
    }
}
