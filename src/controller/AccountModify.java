package controller;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Integer;

import patterns.Singleton;
import model.Student;
import model.Teacher;
import Lib.Validation;
import Lib.Regex;

public class AccountModify {

    // =======================  functions
    public static void deleteAccountStatement(String table, String id) {
        Connection conn = Singleton.getInstance();
        String query = "DELETE FROM " + table + " WHERE id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // input id 
    public static String inputId(String type) {
        Connection conn = Singleton.getInstance();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean flag = false;
        String id = null;

        try {
            do {
                id = Validation.inputString("Enter Id: ", "");
                statement = conn.prepareStatement("SELECT * FROM " + type);
                resultSet = statement.executeQuery();
                // check if the id existed
                while (resultSet.next()) {
                    if (resultSet.getString(1).equals(id)) {
                        flag = true;
                    }
                }
                
                if (!flag) {
                    System.out.println("Id not found. Please try again!");
                }
            }   while (!flag);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    
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

    // =======================  main features
    public static void addingAccount() { 
        Connection conn = Singleton.getInstance();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String lastID = null;

        String type = Validation.inputString("Enter role (teacher, student): ", "teacher|student");
        try {
            if (type.equals("student")) {
                Student student = new Student();
                statement = conn.prepareStatement("SELECT TOP 1 * FROM Student WHERE major_id = ? ORDER BY id DESC");
                
                // input object 
                student.setName(Validation.inputString("Enter student name: ", ""));
                student.setDateOfBirth(Validation.inputDate("Enter date of birth (yyyy-mm-dd): ", Regex.DATE_PATTERN));
                student.setMail(Validation.inputString("Enter email: ", Regex.EMAIL_PATTERN));
                student.setMajorID(Validation.inputString("Enter Major: ", ""));

                // find the last student in table
                statement.setString(1, student.getMajorID());
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    lastID = resultSet.getString(1);
                } else {
                    lastID = "160000";
                }
                System.out.println(lastID);
                // generate id
                student.setId(generateId(lastID, student.getMajorID(), "\\d+"));
                
                // insert to sql 
                statement = conn.prepareStatement("INSERT INTO student VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, student.getId());
                statement.setString(2, student.getName());
                statement.setString(3, Validation.convertDateFormat(student.getDateOfBirth(), Regex.DATE_PATTERN));
                statement.setString(4, "1234");
                statement.setString(5, student.getMail());
                statement.setString(6, student.getMajorID());
                statement.executeUpdate();
                System.out.println("Successful change");
            } else {
                Teacher teacher = new Teacher();
                String[] partsName;
                String lastName = null;
                // input 
                teacher.setName(Validation.inputString("Enter name: ", ""));
                teacher.setMail(Validation.inputString("Enter mail: ", Regex.EMAIL_PATTERN));
                
                // find the last id 
                partsName = teacher.getName().split(" ");
                lastName = partsName[partsName.length - 1];

                statement = conn.prepareStatement("SELECT TOP 1 * FROM Teacher WHERE id LIKE ? ORDER BY id DESC");
                statement.setString(1, lastName + "%");
                resultSet = statement.executeQuery();
                if (resultSet.next()){
                    lastID = resultSet.getString(1);
                } else {
                    lastID = "0";
                }
                //generate Id
                teacher.setId(generateId(lastID, lastName, "\\d+"));
                
                // Insert to table
                statement = conn.prepareStatement("INSERT INTO Teacher VALUES (?, ?, ?, ?)");
                statement.setString(1, teacher.getId());
                statement.setString(2, teacher.getName());
                statement.setString(3, teacher.getMail());
                statement.setString(4, "1234");
                statement.executeUpdate();

                System.out.println("Successful change");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteAccount() {
        String type = Validation.inputString("Enter role (teacher, student): ", "teacher|student");
        try {
            String id = inputId(type);
            deleteAccountStatement(type, id);
            System.out.println("Successful change");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
