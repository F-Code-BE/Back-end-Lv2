package Lib;

public class Regex {
    public static final String STUDENT_ID_PATTERN = "(SE|se)\\d{6}";
    public static final String DATE_PATTERN = "yyyy-mm-dd";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String EMAIL_PATTERN = "^(([^<>()[\\]\\.,;:\\s@\\\"]+(\\.[^<>()[\\]\\.,;:\\s@\\\"]+)*)|(\\\".+\\\"))@(([^<>()[\\]\\.,;:\\s@\\\"]+\\.)+[^<>()[\\]\\.,;:\\s@\\\"]{2,})$";
    public static final String PASSWORD_PATTERN = "[\\w\\d\\D\\W\\s]{8,}";
    
}
