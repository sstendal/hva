package hva;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void info(String msg) {
        System.out.println(format.format(new Date()) + ": " + msg);
    }

    public static void error(String msg, Exception e) {
        System.err.println(format.format(new Date()) + ": " + "ERROR: " + msg);
        e.printStackTrace(System.err);
    }

    public static void error(String msg) {
        System.err.println(format.format(new Date()) + ": " + "ERROR: " + msg);
    }
}
