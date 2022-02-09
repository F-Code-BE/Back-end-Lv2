package controller;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Integer;

import patterns.Singleton;
import model.Student;
import Lib.Validation;
import Lib.Regex;

public class AccountModify {
    
    public static String generateId(String preId, String prefix, String patterns) {
        Pattern p = Pattern.compile(patterns);
        Matcher m = p.matcher(preId);
        int number = 0;
        String result = prefix;
        if (m.find()) {
            number = Integer.parseInt(m.group(0));
            number++;
        }
        if (number < 10) {
            result += '0';
        }
        result += Integer.toString(number);
        return result;
    }
    public static void addingAccount(String type) { 
        try {
            if (type.equals("student")) {
                Student student = new Student();
                Connection conn = Singleton.getInstance();
                PreparedStatement statement = conn.prepareStatement("SELECT TOP 1 * FROM Student WHERE major_id = ? ORDER BY id DESC");
                ResultSet resultSet = null;
                String lastID = null;
                
                // input object 
                student.setName(Validation.inputString("Enter student name: ", ""));
                student.setDateOfBirth(Validation.inputDate("Enter date of birth (yyyy-mm-dd): ", Regex.DATE_PATTERN));
                student.setMail(Validation.inputString("Enter email: ", Regex.EMAIL_PATTERN));
                student.setMajorID(Validation.inputString("Enter Major: ", ""));

                // find the last student in table
                statement.setString(1, student.getMajorID());
                resultSet = statement.executeQuery();
                resultSet.next();
                lastID = resultSet.getString(1);
                System.out.println(lastID);
                // generate id
                student.setId(generateId(lastID, student.getMajorID(), "\\d+"));
                
                // check information
                System.out.println("======== Student information =========");
                System.out.println(student.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        addingAccount("student");
    }
}
