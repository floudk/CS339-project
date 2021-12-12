package yfqing.cs339.pa.encode;

import java.util.Calendar;
import java.util.Scanner;

public class encode {
    static Scanner input = new Scanner(System.in);

    public static String eenc(String s) {
        s=s.toLowerCase();
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        // Date d = new Date(System.currentTimeMillis());
        // SimpleDateFormat sbf = new SimpleDateFormat("MM");
        // System.out.println(sbf.format(d));
        // System.out.println(month);
        char s0 = s.charAt(0), s1 = s.charAt(1), s2 = s.charAt(2), s3 = s.charAt(3);
        if (s0 <= 57 && s0 >= 48) {
            while (month >= 10)
                month -= 10;
            s0 += month;
            while (s0 > 57)
                s0 -= 10;
        } else {
            s0 -= month;
            while (s0 < 97)
                s0 += 26;
        }
        if (s1 <= 57 && s1 >= 48) {
            while (day >= 10)
                day -= 10;
            s1 += day;
            while (s1 > 57)
                s1 -= 10;
        } else {
            s1 -= day;
            while (s1 < 97)
                s1 += 26;
        }

        char[] r = { s0, s1, s2, s3 };
        String ss = new String(r);
        return ss;
    }

}
