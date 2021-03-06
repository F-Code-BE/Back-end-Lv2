package view;

import java.util.Scanner;
import java.util.Vector;

import Lib.Validation;

public class FapMenu extends Vector<String> {
    Scanner sc = new Scanner(System.in);

    public FapMenu() {
        super();
    }

    // Print main menu and get user's choice>
    public int getUserChoice() {
        int i = 1;
        System.out.println("");
        for (String option : this) {
            System.out.println(i + ". " + option);
            i++;
        }
        return Validation.inputInt("Enter your choice: ");
    }

}