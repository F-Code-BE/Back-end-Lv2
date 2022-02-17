package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.Date;

import model.Student;
import view.FapMenu;
import Lib.Regex;
import Lib.Validation;
import patterns.Singleton;

public class StudentRequest {

    private String userId;
    private Student user;
    private String name;
    private String email;
    private Date dateOfBirth;
    private String majorId;
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    
    public Vector<String> getData(String query, String... params) {
        conn = Singleton.getInstance();
        Vector<String> result = new Vector<String>();

        try {
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
    public String getMajorId() {
        String query = "SELECT id FROM major";
        Vector<String> majors = new Vector<String>();
        majors = getData(query);

        do {
            majorId = Validation.inputString("Enter major id: ", "");
            
            // check if that classid existed
            if (majors.contains(majorId)) {
                return majorId;
            }
            System.out.println("Invalid Id. Please enter again!");
        } while (true);
    }


    public void changeInfo() {
        user.setName(Validation.inputString("Enter new name (type null to skip): ", "([\\w|\\s]+)"));
        user.setMail(Validation.inputString("Enter new email (type null to skip): ", Regex.EMAIL_PATTERN + "|null"));
        user.setDateOfBirth(Validation.inputDate("Enter new date of birth (type null to skip): "));
        user.setMajorID(getMajorId());
    
        // save to table 
        System.out.println(user.toString());
    }

    public static void main(String[] args) {
        StudentRequest test = new StudentRequest();
        int choice = 0;
        FapMenu menu = new FapMenu();

        menu.add("Change Information");
        menu.add("Retake");
        menu.add("Check Attendance");
        menu.add("Alternating class");
        do {
            choice = menu.getUserChoice();
            switch (choice) {
                case 1:
                    test.changeInfo();
                    break;
                default:
                    break;
            }
        } while (choice <= 4);
    }
}