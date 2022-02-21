package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.*;
import Lib.*;
import patterns.Singleton;

public class ViewInfo {
    public void viewStudentInfo(String studentId) {
        Connection conn = Singleton.getInstance();

        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT [name] ,[date_of_birth],[password],[mail],[major_id]FROM Student WHERE id = ?");
            statement.setString(1, studentId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                System.out.println("Name: " + result.getString("name"));
                System.out.println("Date of birth: " + result.getString("date_of_birth"));
                System.out.println("Password: " + result.getString("password"));
                System.out.println("Mail: " + result.getString("mail"));
                System.out.println("Major: " + result.getString("major_id"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void viewSyllabus(String studentId) {
        Connection conn = Singleton.getInstance();
        // get major id from student
        String majorId = "";
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT major_id FROM Student WHERE id = ?");
            statement.setString(1, studentId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                majorId = result.getString("major_id");
            }

            statement = conn.prepareStatement(
                    "SELECT [course_id],[course_prequisite],[alternative_course] FROM Major_course WHERE major_id = ?");
            statement.setString(1, majorId);
            result = statement.executeQuery();
            while (result.next()) {
                System.out.println("CourseId: " + result.getString("course_id"));
                System.out.println("CoursePrequisite: " + result.getString("course_prequisite"));
                System.out.println("AlternativeCourse: " + result.getString("alternative_course"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void viewAttendance(String studentId) {
        // get all course of student
        Connection conn = Singleton.getInstance();
        // list of class
        ArrayList<String> classIds = new ArrayList<>();
        String classId = "";
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT class_id FROM Class_student WHERE student_id = ?");
            statement.setString(1, studentId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                classId = result.getString("class_id");
                classIds.add(classId);
            }

            // let student choose class
            System.out.println("---------------------------");
            System.out.println("   List of your class");
            for (int i = 0; i < classIds.size(); i++) {
                System.out.println(i + 1 + " " + classIds.get(i));
            }
            int userChoice;
            do {
                userChoice = Validation.inputInt("Please enter your choice: ");
                if (userChoice > classIds.size()) {
                    System.out.println("Please enter valid number!");
                }
            } while (userChoice > classIds.size());

            // get all slot of class
            statement = conn.prepareStatement(
                    "SELECT [slot_id], [status] FROM Attendance WHERE slot_id LIKE ? AND student_id = ?");
            statement.setString(1, classIds.get(userChoice - 1) + "%");
            statement.setString(2, studentId);
            result = statement.executeQuery();
            while (result.next()) {
                System.out.print("SlotId: " + result.getString("slot_id") + " ");
                System.out.println("Status: " + result.getString("status"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void viewMarkReport(String studentId) {
        // get all course of student
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT class_id, gpa, status FROM Mark WHERE student_id = ?");
            statement.setString(1, studentId);
            ResultSet result = statement.executeQuery();
            // print result
            while (result.next()) {
                System.out.println("ClassId: " + result.getString("class_id"));
                System.out.println("GPA: " + result.getString("gpa"));
                System.out.println("Status: " + result.getString("status"));
                System.out.println("---------------------------");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void viewTimetable(String studentId) {
        // get all course of student
        Connection conn = Singleton.getInstance();
        // list of class
        try {
            // show time table
            ArrayList<Timetable> timetable = new ArrayList<>();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class_student cs JOIN Class c ON cs.class_id = c.id JOIN Slot_type st ON cs.class_id = st.class_id WHERE cs.student_id = ?");
            statement.setString(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("ALL COURSE:");
            while (resultSet.next()) {
                Timetable timetableModel = new Timetable();
                timetableModel.setGroupId(resultSet.getString(1));
                timetableModel.setCourseId(resultSet.getString(2));
                timetableModel.setSlotType(resultSet.getString(3));
                timetableModel.setSemesterId(resultSet.getString(4));
                timetableModel.setMaxStudent(Integer.parseInt(resultSet.getString(5)));
                timetable.add(timetableModel);
            }
            for (Timetable t : timetable) {
                System.out.println(t.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void viewTeacherInfo(String teacherId) {
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT id, name, password, mail FROM Teacher WHERE id = ?");
            statement.setString(1, teacherId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                System.out.println("Id: " + result.getString("id"));
                System.out.println("Name: " + result.getString("name"));
                System.out.println("Password: " + result.getString("password"));
                System.out.println("Mail: " + result.getString("mail"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}