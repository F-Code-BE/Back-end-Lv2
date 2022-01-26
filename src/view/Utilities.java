package view;

import java.util.Scanner;

public class Utilities {
    public static int getInt(String message, boolean isReguiredPosive) {
        boolean cont = false;
        int value = 0;
        do {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print(message);
            value = sc.nextInt();
            if (isReguiredPosive == true && value <= 0) {
                throw new Exception();
            }
            cont = false;
        } catch(Exception e) {
            System.out.println("Enter valid value");
            cont = true;
        }
        } while (cont);
        return value;
    }
}
