package controller;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import Lib.Regex;
import Lib.Validation;
import model.Slot;
import patterns.Singleton;
import view.FapMenu;

public class CheckAttendence {
    private String teacherId;
    private String classId;
    private byte slot;
    private Date date;
    
    public CheckAttendence(String teacherId) {
        this.teacherId = teacherId;
    }

   
    private void getClassAndSlot() {
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("select class_id, slot  from slot where teacher_id = ? and date = ?");
            stmt.setString(1, teacherId);
            stmt.setDate(2, (java.sql.Date) date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getInt(1));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void showMenu() {
       date = Validation.inputDate("Enter date you want to check: ");
       getClassAndSlot();
    }
}
