package Lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Validation {

    private static Scanner sc = new Scanner(System.in);

    public static int inputNumber(String message) {
        int number = -1;
        boolean flag = false;
        do {
            System.out.print(message);
            try {
                number = Integer.parseInt(sc.nextLine());
                if (number < 0) {
                    throw new Exception();
                }
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        return number;
    }

    public static int inputNumber(String message, int oldValue) {
        int number = -1;
        boolean flag = false;
        do {
            System.out.print(message);
            try {
                String result = sc.nextLine();
                if (result.isEmpty())
                    return oldValue;
                number = Integer.parseInt(result);
                if (number < 0) {
                    throw new Exception();
                }
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        return number;
    }

    public static String inputString(String message, String pattern) {
        String result = null;
        boolean flag = false;

        do {
            System.out.print(message);
            try {
                result = sc.nextLine();
                if (result.length() == 0)
                    throw new Exception();
                if (pattern != "" && !result.matches(pattern))
                    throw new Exception();
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        return result;

    }

    public static String inputString(String message, String oldValue, String pattern) {
        String result = null;
        boolean flag = false;

        do {
            System.out.print(message);
            try {
                result = sc.nextLine();
                if (result.isEmpty())
                    return oldValue;
                if (pattern != "" && !result.matches(pattern))
                    throw new Exception();
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        return result;

    }

    public static boolean inputBoolean(String message) {
        boolean flag = false;
        boolean results;
        do {
            System.out.print(message);
            String str = sc.nextLine();

            try {
                if (!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false"))
                    throw new Exception();
                results = Boolean.parseBoolean(str);
                return results;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);

        return false;
    }

    public static double inputFloat(String message) {
        double number = -1;
        boolean flag = false;
        do {
            System.out.print(message);
            try {
                number = Double.parseDouble(sc.nextLine());
                if (number < 0) {
                    throw new Exception();
                }
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        return number;
    }

    public static double inputFloat(String message, double oldValue) {
        double number = -1;
        boolean flag = false;
        do {
            System.out.print(message);
            try {
                String result = sc.nextLine();
                if (result.isEmpty())
                    return oldValue;
                number = Double.parseDouble(result);
                if (number < 0) {
                    throw new Exception();
                }
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        return number;
    }

    public static Date inputDate(String message, String pattern) {
        Date dateResult = null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        boolean flag = false;
        do {
            System.out.print(message);
            try {
                dateResult = formatter.parse(sc.nextLine());
                flag = false;
            } catch (Exception e) {
                System.out.println("Invalid.");
                flag = true;
            }

        } while (flag);

        return dateResult;
    }
    
    public static String convertDateFormat(Date date, String pattern) {
        String dateResult = null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            dateResult = formatter.format(date);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return dateResult;
    }
}
