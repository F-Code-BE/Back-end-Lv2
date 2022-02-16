package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.Student;
import view.FapMenu;
import Lib.Regex;
import Lib.Validation;

public class StudentRequest {

    private Student user;
    private String name;
    private String email;
    private Date dateOfBirth;
    private String majorId;
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    
    public void changeInfo() {
        name = Validation.inputString("Enter new name (type null to skip): ", "([\\w|\\s]+|null)");
        mail = Validation.inputString("Enter new email (type null to skip): ", Regex.EMAIL_PATTERN);

    }

    public static void main(String[] args) {
        int choice = 0;
        FapMenu menu = new FapMenu();

        menu.add("Change Information");
        menu.add("Retake");
        menu.add("Check Attendance");
        menu.add("Alternating class");
        do {
            choice = menu.getChoice();
            switch (choice) {
                case 1:
                    changeInfo();
                    break;
                default:
                    break;
            }
        }
    }
}