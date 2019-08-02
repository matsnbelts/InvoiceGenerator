import java.io.*;
import java.text.ParseException;
import java.util.Map;

public class InvoiceGeneratorApp {
    private static final String CSV_FILE = "/Users/srinis/Documents/MatsNBelts/CustomerDetailsJuly2019.csv";
    private static final String OUTPUT_FOLDER = "/Users/srinis/Documents/MatsNBelts/test-invoice-Jul/";
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(String.format("%.02f", 600f));
        final String fileName = "Report-July19.csv";
        FileWriter csvWriter = new FileWriter(new File(fileName), true);
        String stringBuilder = "";
        stringBuilder = "CustomerId,CustomerName,Email,Mobile,ApartmentNo,ApartmentName,TotalInvoice\n";
        csvWriter.append(stringBuilder);
        csvWriter.close();
        LoadCustomerDetails loadCustomerDetails = new LoadCustomerDetails();
        Map<String, CustomerProfile> customerProfileMap = loadCustomerDetails
                .loadCustomersPaymentDetails(new File(CSV_FILE));

        for(Map.Entry<String, CustomerProfile> customerProfileEntry: customerProfileMap.entrySet()) {
            InvoiceGenerator.InvoiceGeneratorBuilder invoiceGeneratorBuilder = InvoiceGenerator.builder();
            InvoiceGenerator invoiceGenerator = invoiceGeneratorBuilder.customerProfile(customerProfileEntry.getValue())
                    .area("Karapakkam").cityPincode("Chennai - 600097")
                    .destinationFile(OUTPUT_FOLDER + customerProfileEntry.getValue().getCustomerId()+ ".pdf").build();
            invoiceGenerator.generateInvoicePdf();
            invoiceGenerator.generateConsolidatedReport(fileName);

//            String from = "matsnbeltsapp@gmail.com";
//            String pwd = "vsmatsnbelts";
//            String to_mail = customerProfileEntry.getValue().getEmail();
//            if(to_mail.isEmpty()) {
//                to_mail = "johnpraveen@yahoo.com";
//                System.out.println(":::: " + customerProfileEntry.getValue().getCustomerId());
//            }
//            //to_mail = "matsnbeltsapp@gmail.com";
//            Mailer.send(from, pwd, to_mail, "Mats And Belts - Invoice Generated for July'19",
//                    "Hey " + customerProfileEntry.getValue().getCustomerName() +
//                            ",\n This is an automatically generated email. Please do not reply to it.\n",
//                    OUTPUT_FOLDER + customerProfileEntry.getValue().getCustomerId()+ ".pdf", "");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/srinis/Documents/MatsNBelts/JulyCustomerInvoiceMap.csv")));
        String line = "";
        br.readLine();
        while((line = br.readLine()) != null) {
            System.out.println(line);
            String[] row = line.split(",");
            String from = "matsnbeltsapp@gmail.com";
            String pwd = "vsmatsnbelts";
            if(row.length < 3 || row[2].isEmpty()) {
                continue;
            }
            if(!new File(OUTPUT_FOLDER + row[0]+ ".pdf").exists()) {
                continue;
            }
            System.out.println(":::: " + row[0] + " : " + row[1] + " : " + row[2] + " : "
            + new File(OUTPUT_FOLDER + row[0]+ ".pdf").exists());
            //String to_mail = "matsnbeltsapp@gmail.com";
            String to_mail = row[2];

            Mailer.send(from, pwd, to_mail, "Mats And Belts - Invoice Generated for July'19",
                    "Hey " + row[1] +
                            ",\n This is an automatically generated email. Please do not reply to it.\n",
                    OUTPUT_FOLDER + row[0]+ ".pdf", "");
        }

    }
}
