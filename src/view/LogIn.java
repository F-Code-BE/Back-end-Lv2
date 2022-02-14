package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Lib.Regex;
import Lib.Validation;

import patterns.Singleton;

public class LogIn {
    private static String userId;

    public static int logIn() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        String email;
        String password;
        System.out.println("---------------------------------------");
        System.out.println("                LOGIN");
        email = Validation.inputString("email: ", Regex.EMAIL_PATTERN);
        password = Validation.inputString("password: ", Regex.PASSWORD_PATTERN);
        Connection conn = Singleton.getInstance();
        try {
            Statement stmt = conn.createStatement();
            String selectQuery = "select mail, password, id from ";
            ResultSet rs = stmt.executeQuery(selectQuery + "student");
            // show data
            while (rs.next()) {
                
                if (email.equalsIgnoreCase(rs.getString(1)) && password.equals(rs.getString(2))) {
                    setUserId(rs.getString(3));
                    return 1;
                }
            }
            rs = stmt.executeQuery(selectQuery + "teacher");
            while (rs.next()) {
                if (email.equalsIgnoreCase(rs.getString(1)) && password.equals(rs.getString(2))) {
                    setUserId(rs.getString(3));
                    return 2;
                }
            }
            rs = stmt.executeQuery(selectQuery + "admin");
            while (rs.next()) {
                if (email.equalsIgnoreCase(rs.getString(1)) && password.equals(rs.getString(2))) {
                    setUserId(rs.getString(3));
                    return 3;
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return 0;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String id) {
        userId = id;
    }
}
    