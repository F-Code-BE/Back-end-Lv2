package Lib;

import java.text.SimpleDateFormat;

public class Regex {
    public static final String STUDENT_ID_PATTERN = "(SE|se)\\d{6}";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String EMAIL_PATTERN = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    public static final String PASSWORD_PATTERN = "[\\w\\d\\D\\W\\s]{3,}";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_PATTERN);
}
