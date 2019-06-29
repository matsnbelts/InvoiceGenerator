import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import lombok.Builder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Builder
public class InvoiceGenerator {
    public static final String DEST = "simple_rowspan_colspan.pdf";
    private String destinationFile;
    private CustomerProfile customerProfile;
    private String area;
    private String cityPincode;

    public void generateInvoicePdf() throws IOException {
        if(area == null) {
            area = "Karapakkam";
        }
        if(cityPincode == null) {
            cityPincode = "Chennai - 600097";
        }
        createPdf(destinationFile);
    }

    private void createPdf(String dest) throws IOException {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        addInvoiceHeader(document); //add invoice head title
        setCustomerDetailsLayout(document); // set customer account details
        float[] col_widths = {40, 40};
        Table table = new Table(col_widths);
        table.setWidth(UnitValue.createPointValue(200));
        System.out.println(table.getWidth());
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        DeviceRgb lightGrey = new DeviceRgb(227, 225, 225);
        table.addCell(getCell(1, "Customer Id", lightGrey, TextAlignment.LEFT));
        table.addCell(getCell(1, customerProfile.getCustomerId(), null, TextAlignment.RIGHT));

        table.addCell(getCell(1, "Invoice #",lightGrey, TextAlignment.LEFT));
        table.addCell(getCell(1, "MNB-19-" +  System.currentTimeMillis(), null, TextAlignment.RIGHT));

        table.addCell(getCell(1, "Invoice Date", lightGrey, TextAlignment.LEFT));
        table.addCell(getCell(1, "June 30, 2019", null, TextAlignment.RIGHT));

        table.addCell(getCell(1, "Payment Due Date", lightGrey, TextAlignment.LEFT));
        table.addCell(getCell(1, "July 05, 2019", null, TextAlignment.RIGHT));
        document.add(table);
        setUnitsLayout(document);
        setFooterLayout(document);
        document.close();

    }

    private void addInvoiceHeader(Document document) throws IOException {
        Paragraph para = new Paragraph("I N V O I C E");
        para.setTextAlignment(TextAlignment.CENTER);
        para.setBackgroundColor(Color.BLACK);
        para.setFontColor(Color.WHITE);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        para.setFont(bold);
        para.setFontSize(12);
        document.add(para);
    }

    private void setUnitsLayout(Document document) throws IOException {
        //Description,CarNo,CarModel,CarType,Period,Quantity,Unit,Price
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 1, 2, 1, 1, 1}));
        table.setMarginTop(20);
        setUnitsHeader(table);
        populateCarsTableData(table);
        document.add(table);
    }

    private void populateCarsTableData(Table table) throws IOException {
        Cell cell;
        int i = 1;
        float totalAmount = 0;
        for(CustomerCar car : customerProfile.getCars()) {
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph("Car Subscription " + i, TextAlignment.LEFT));
            table.addCell(cell);
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph(car.getCarNo(), TextAlignment.LEFT));
            table.addCell(cell);
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph(car.getCarModel(), TextAlignment.LEFT));
            table.addCell(cell);
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph(car.getCarType(), TextAlignment.LEFT));
            table.addCell(cell);
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph(car.getStartDate() + " - " + "30th June, 2019", TextAlignment.LEFT));
            table.addCell(cell);
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph("1", TextAlignment.RIGHT));
            table.addCell(cell);
            float floatPrice = Float.parseFloat(car.getPriceCharge());
            totalAmount += floatPrice;
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph(String.format("%.02f", floatPrice), TextAlignment.RIGHT));
            table.addCell(cell);
            cell = new Cell(1, 1).add(
                    getUnitsColumnParagraph(String.format("%.02f", floatPrice), TextAlignment.RIGHT));
            table.addCell(cell);
            i++;
        }
        Paragraph matsnbeltspara = new Paragraph("Thanks for your business!");
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC);
        matsnbeltspara.setFont(bold);
        matsnbeltspara.setMarginTop(20);
        matsnbeltspara.setFontSize(8);
        matsnbeltspara.setFontColor(Color.BLUE);

        matsnbeltspara.setTextAlignment(TextAlignment.CENTER);
        cell = new Cell(2, 5).add(matsnbeltspara);
        table.addCell(cell);
        ///// subtotal
        cell = new Cell(1, 2).add(getUnitsColumnParagraph("Subtotal:", TextAlignment.LEFT));
        cell.setBorderRight(Border.NO_BORDER);
        table.addCell(cell);
        cell = new Cell(1, 1).add(
                getUnitsColumnParagraph(String.format("%.02f", totalAmount), TextAlignment.RIGHT));
        cell.setBorderLeft(Border.NO_BORDER);
        table.addCell(cell);
        ////// total
        ///// subtotal
        cell = new Cell(1, 2).add(getUnitsColumnParagraph("Total:", TextAlignment.LEFT));
        cell.setBorderRight(Border.NO_BORDER);
        table.addCell(cell);
        cell = new Cell(1, 1).add(
                getUnitsColumnParagraph(String.format("%.02f", totalAmount), TextAlignment.RIGHT));
        cell.setBorderLeft(Border.NO_BORDER);
        table.addCell(cell);
    }

    private Paragraph getUnitsColumnParagraph(String text, TextAlignment alignment) {
        Paragraph para = new Paragraph(text);
        para.setTextAlignment(alignment);
        return para;
    }

    private void setUnitsHeader(Table table) {
        Cell cell;
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Description"));
        DeviceRgb lightGrey = new DeviceRgb(227, 225, 225);
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Car No."));
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Car Model"));
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Car Type"));
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Period"));
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Qty."));
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Unit"));
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
        cell = new Cell(1, 1).add(getUnitsHeaderParagraph("Price"));
        cell.setBackgroundColor(lightGrey, 0.5f);
        table.addCell(cell);
    }

    private Paragraph getUnitsHeaderParagraph(String text) {
        Paragraph para = new Paragraph(text);
        para.setBold();
        para.setTextAlignment(TextAlignment.CENTER);
        return para;
    }

    private void setFooterLayout(Document document) {
        Paragraph para = new Paragraph("");
        para.setTextAlignment(TextAlignment.CENTER);
        para.setFontSize(11);
        document.add(para);
        para = new Paragraph("If you have any questions about this invoice, please contact");
        para.setTextAlignment(TextAlignment.CENTER);
        para.setFontSize(11);
        document.add(para);
        para = new Paragraph("9840736881");
        para.setTextAlignment(TextAlignment.CENTER);
        para.setFontSize(11);
        document.add(para);
    }

    private void setCustomerDetailsLayout(Document document) throws IOException {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        table.setKeepTogether(true);
        table.setBorder(Border.NO_BORDER);
        Cell cell;
        cell = new Cell(1, 1).add(new Paragraph(customerProfile.getCustomerName()));
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);

        Paragraph matsnbeltspara = new Paragraph("MATS N BELTS");
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        matsnbeltspara.setFont(bold);
        matsnbeltspara.setTextAlignment(TextAlignment.CENTER);
        matsnbeltspara.setFontColor(new DeviceRgb(219, 59, 50));
        matsnbeltspara.setFontSize(16);
        matsnbeltspara.setUnderline();
        cell = new Cell(6, 1).add(matsnbeltspara);
        cell.setMarginTop(40);
        cell.setBorder(Border.NO_BORDER);

        table.addCell(cell);

        cell = new Cell(1, 1).add(new Paragraph(
                customerProfile.getApartmentNo() + ", " + customerProfile.getApartment()));
        cell.setBorder(Border.NO_BORDER);

        table.addCell(cell);
        cell = new Cell(1, 1).add(new Paragraph(area));
        cell.setBorder(Border.NO_BORDER);

        table.addCell(cell);
        cell = new Cell(1, 1).add(new Paragraph(cityPincode));
        cell.setBorder(Border.NO_BORDER);

        table.addCell(cell);
        cell = new Cell(1, 1).add(new Paragraph(customerProfile.getMobile()));
        cell.setBorder(Border.NO_BORDER);

        table.addCell(cell);
        Paragraph mailPara = new Paragraph(customerProfile.getEmail());
        mailPara.setUnderline();
        mailPara.setFontColor(Color.BLUE);
        cell = new Cell(1, 1).add(mailPara);
        cell.setBorder(Border.NO_BORDER);

        table.addCell(cell);

        document.add(table);

    }

    private Cell getCell(int colspan, String text, Color bgColor, TextAlignment textAlignment) {
        Cell cell = new Cell(1, colspan);
        Paragraph p = new Paragraph(text);
        //p.setFontSize(8);
        if(bgColor != null) {
            cell.setBackgroundColor(bgColor, 0.5f);
        }
        p.setTextAlignment(textAlignment);
        cell.add(p);
        return cell;
    }
}
