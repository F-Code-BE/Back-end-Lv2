package Lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Validation {

    private static Scanner sc = new Scanner(System.in);

    public static int inputInt(String message) {
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

    public static String inputString(String message, String pattern) {
        String result = null;
        boolean flag = false;

        do {
            System.out.print(message);
            try {
                result = sc.nextLine();
                if (result.length() == 0)
                    throw new Exception();
                if (!"".equals(pattern) && !result.matches(pattern))
                    throw new Exception();
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        return result;

    }

    public static Boolean confirmYesNo(String message) {
        String result = null;
        boolean flag = false;
        do {
            System.out.print(message);
            try {
                result = sc.nextLine();
                if (result.length() == 0)
                    throw new Exception();
                if (!result.equalsIgnoreCase("y") && !result.equalsIgnoreCase("n") && !result.equalsIgnoreCase("yes")
                        && !result.equalsIgnoreCase("no"))
                    throw new Exception();
                flag = false;
            } catch (Exception e) {

                flag = true;
                System.out.println("Invalid.");
            }
        } while (flag);
        if (result.equalsIgnoreCase("y") || result.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }
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

    public static String inputDate(String message, String pattern) {
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

        return formatter.format(dateResult);
    }
}
