import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTest {

    @Test
    public void test1(){

//        System.out.println("======================");
//        Date date = new Date();
//        System.out.println(date);
//        System.out.println(date.getYear()+1900);
//        System.out.println(date.getMonth()+1);
//
//        System.out.println("======================");
//        LocalDate localDate = LocalDate.now();
//        System.out.println(localDate);
//        System.out.println(localDate.getMonth());
//        System.out.println(localDate.getMonthValue());
//        System.out.println(localDate.getYear());
//        System.out.println(localDate.getDayOfMonth());
//        System.out.println("======================");
//        LocalDate localDate1 = LocalDate.of(2021, 11, 11);
//        System.out.println(localDate1);
//        System.out.println("======================");
//        LocalDateTime localDateTime= LocalDateTime.now();
//        System.out.println(localDateTime);
//        String formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
//        System.out.println(formatTime);
        LocalDateTime time=LocalDateTime.of(2021,9,9,20,0);
        LocalDateTime time2=LocalDateTime.of(2021,9,9,20,0);
        if (time.isAfter(time2)){
            System.err.println("time晚于time2");
        }else {
            System.err.println("time不晚于time2");
        }




    }



}
