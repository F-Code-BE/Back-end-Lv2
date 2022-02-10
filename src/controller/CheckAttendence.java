package controller;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Lib.Validation;
import patterns.Singleton;
import view.FapMenu;

public class CheckAttendence {
    private String teacherId;
    private String classId;
    private String slotId;
    private int slot;
    private Date date;
    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<String> studentIds = new ArrayList<>();

    public CheckAttendence(String teacherId) {
        this.teacherId = teacherId;
    }

    private void getClassAndSlot() {
        var slots = new ArrayList<Integer>();
        var classIds = new ArrayList<String>();
        var slotIds = new ArrayList<String>();
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn
                    .prepareStatement("select class_id, slot, id  from slot where teacher_id = ? and date = ?");
            stmt.setString(1, teacherId);
            var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);
            stmt.setString(2, strDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classIds.add(rs.getString(1));
                slots.add(rs.getInt(2));
                slotIds.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        var menu = new FapMenu();
        for (int i = 0; i < slots.size(); i++)
            menu.add(classIds.get(i) + " Slot: " + slots.get(i));
        int userChoice = menu.getUserChoice();
        classId = classIds.get(userChoice - 1);
        slot = slots.get(userChoice - 1);
        slotId = slotIds.get(userChoice - 1);
    }

    private void showStudentList() {
        System.out.println("---------------------------");
        System.out.println("   List of your student");
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "select Student.id, Student.name from Student where id in ( select student_id from Class_student where class_id = ?)");

            stmt.setString(1, classId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String studentId = rs.getString(1);
                System.out.println(studentId + " " + rs.getString(2));
                studentIds.add(studentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getDates() {
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("select distinct date from slot where teacher_id = ? ");
            stmt.setString(1, teacherId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dates.add(rs.getDate(1));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private void checkPresent() {
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("update attendance set status = ? where slot_id = ?");
            stmt.setString(1, "PRESENT");
            stmt.setString(2, slotId);
            stmt.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private void checkAbsence() {
        String absentStudent;
        Connection conn = Singleton.getInstance();
        while (true) {
            absentStudent = Validation.inputString("Enter student id of absent student(0 to finish): ",
                    "^SE[0-9]{6}$|^se[0-9]{6}$|^0$");
            absentStudent = absentStudent.toUpperCase();
            if (absentStudent.equals("0"))
                break;
            if (!studentIds.contains(absentStudent)) {
                System.out.println("Student not int this class.");
                continue;
            }
            try {
                PreparedStatement stmt = conn
                        .prepareStatement("update attendance set status = ? where slot_id = ? and student_id = ?");

                stmt.setString(1, "ABSENT");
                stmt.setString(2, slotId);
                stmt.setString(3, absentStudent);
                stmt.executeUpdate();
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }

    }

    public void showMenu() {
        getDates();
        var flag = false;
        do {
            date = Validation.inputDate("Enter date you want to check: ");
            if (dates.contains(date)) {
                flag = true;
            } else {
                System.out.println("You don't have any class schedule this date");
            }
        } while (!flag);
        getClassAndSlot();
        showStudentList();
        checkPresent();
        checkAbsence();
    }
}
