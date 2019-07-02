import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException {
        String d = "31-May-19";
        Date dt = new SimpleDateFormat("dd-MMM-yy").parse(d);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(totalDays);
        if (dt.before(new SimpleDateFormat("dd-MMM-yy").parse("01-Jun-19"))
        ) {
            System.out.println("yesssss");

        }
    }
}
