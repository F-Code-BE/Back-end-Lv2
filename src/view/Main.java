package view;

import controller.*;
import model.Timetable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import Lib.Validation;
import patterns.Singleton;

public class Main {
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
            Vector<Timetable> Timetable = new Vector<>();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class_student cs JOIN Class c ON cs.class_id = c.id JOIN Slot_type st ON cs.class_id = st.class_id WHERE cs.student_id = ?");
            statement.setString(1, studentId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("ALL COURSE:");
            while (resultSet.next()) {
                Timetable TimetableModel = new Timetable();
                TimetableModel.setGroupId(resultSet.getString(1));
                TimetableModel.setCourseId(resultSet.getString(2));
                TimetableModel.setSlotType(resultSet.getString(3));
                TimetableModel.setSemesterId(resultSet.getString(4));
                TimetableModel.setMaxStudent(Integer.parseInt(resultSet.getString(5)));
                Timetable.add(TimetableModel);
            }
            for (Timetable t : Timetable) {
                System.out.println(t.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {

        while (true) {
            int userCase = LogIn.logIn();
            var menu = new FapMenu();
            int userChoice;
            if (userCase == 1) {
                menu.add("View information");
                menu.add("View curriculum");
                menu.add("View attendence report");
                menu.add("View mark report");
                menu.add("View academic transcript");
                menu.add("View Timetable");
                menu.add("Change group");
                menu.add("Suspend one semester");
                menu.add("Change information");
                menu.add("Request to ignore attendence");
                menu.add("Log out");
                do {
                    userChoice = menu.getUserChoice();
                    System.out.println(LogIn.getUserId());
                    switch (userChoice) {
                        case 7:
                            ChangeGroup.getAllCourses(LogIn.getUserId());
                            break;
                        default:
                            break;
                    }

                } while (userChoice != 11);
            } else if (userCase == 2) {
                menu.add("View Timetable");
                menu.add("Check attendance");
                menu.add("Change information");
                menu.add("Change subject");
                menu.add("Change slot");
                menu.add("Enter marks");
                menu.add("Log out");
                do {
                    userChoice = menu.getUserChoice();
                    switch (userChoice) {
                        case 2:
                            var checkAttendence = new CheckAttendence(LogIn.getUserId());
                            checkAttendence.showMenu();
                            break;
                        case 6:
                            var inputMark = new InputMark(LogIn.getUserId());
                            inputMark.showMenu();
                        default:
                            break;
                    }
                } while (userChoice != 7);

            } else if (userCase == 3) {
                menu.add("Add account");
                menu.add("Delete account");
                menu.add("Update information base on request");
                menu.add("Log out");
                do {
                    userChoice = menu.getUserChoice();
                    switch (userChoice) {
                        case 1:
                            AccountModify.addingAccount();
                            break;
                        case 2:
                            AccountModify.deleteAccount();
                            break;
                        default:
                            break;
                    }
                } while (userChoice != 4);
            } else {
                break;
            }
        }
    }
}