package controller;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Lib.Validation;
import model.Student;
import patterns.Singleton;
import view.FapMenu;

public class CheckAttendence {
    private String teacherId;
    private String classId;
    private String slotId;
    private int slot;
    private Date date;
    private ArrayList<Student> students = new ArrayList<Student>();
    public CheckAttendence(String teacherId) {
        this.teacherId = teacherId;
    }

   
    private void getClassAndSlot() {
        var slots = new ArrayList<Integer>();
        var classIds = new ArrayList<String>();
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("select class_id, slot  from slot where teacher_id = ? and date = ?");
            stmt.setString(1, teacherId);
            var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);
            stmt.setString(2, strDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classIds.add(rs.getString(1));
                slots.add(rs.getInt(2));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        var menu = new FapMenu();
        for (int i = 0; i < slots.size(); i++)
            menu.add(classIds.get(i) + " Slot: " + slots.get(i));
        int userChoice = menu.getUserChoice();
        classId = classIds.get(userChoice - 1);
        slot = slots.get(userChoice - 1);
       
    }

    private void showStudentList() {
        System.out.println("---------------------------");
        System.out.println("   List of your student");
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("select Student.id, Student.name from Student where id in ( select student_id from Class_student where class_id = ?)");

            stmt.setString(1, classId);
           
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
              System.out.println(rs.getString(1) + " " + rs.getString(2));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void showMenu() {
       date = Validation.inputDate("Enter date you want to check: ");
       getClassAndSlot();
       showStudentList();
    }
}
