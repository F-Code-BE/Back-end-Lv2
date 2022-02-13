package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import Lib.Validation;
import patterns.Singleton;

public class InputMark {

    private Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String userId;
    private String classId;
    private String studentId;
    // TODO: using model for this
    Vector<String> classes = new Vector<String>();
    Vector<String> students = new Vector<String>();
    Vector<String> studentMarks = new Vector<String>();

    public InputMark() {
    }

    public InputMark(String userId) {
        this.userId = userId;
    }

    public void inputClassId() {
        do {
            classId = Validation.inputString("Enter Class id: ", "");
            // check if that classid existed
            if (classes.contains(classId)) {
                return;
            }
            System.out.println("Invalid Id. Please enter again!");
        } while (true);
    }

    public void inputStudentId() {
        do {
            studentId = Validation.inputString("Enter student id: ", "");
            // check if that classid existed
            if (students.contains(studentId)) {
                return;
            }
            System.out.println("Invalid Id. Please enter again!");
        } while (true);
    }

    public double inputStudentMark() {
        double result = 0;
        do {
            result = Validation.inputFloat("Enter gpa: ");
            // validate result
            if (result <= 10) {
                return result;
            }
            System.out.println("Invalid Mark. Please enter again!");
        } while (true);
    }

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

    public void executeUpdateData(String query, String... params) {
        conn = Singleton.getInstance();

        try {
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void printClasses() {
        String query = "SELECT DISTINCT c.id FROM slot s JOIN Class c on s.class_id = c.id WHERE s.teacher_id = ?";
        classes = getData(query, userId);

        System.out.println("All class: ");
        for (String id : classes) {
            System.out.println(id);
        }
    }

    public void printStudents() {
        String query = "SELECT student_id FROM Class_student WHERE class_id = ?";
        students = getData(query, classId);

        System.out.println("All students: ");
        for (String id : students) {
            System.out.println(id);
        }
    }

    public String checkStatus(double gpa) {
        Vector<String> attendances = new Vector<String>();

        if (gpa < 5) {
            return "Not Pass";
        }
        // get all attendances 
        try {
            int absentCounter = 0;
            String query = "SELECT a.status, s.date FROM Attendance a JOIN slot s ON a.slot_id = s.id JOIN Class c ON c.id = s.class_id WHERE a.student_id = ? AND c.id = ?";
            attendances = getData(query, studentId, classId);
            for (String slot : attendances) {
                absentCounter += slot.matches("absent|ABSENT") ? 1 : 0;
            }
            if (absentCounter > 1) {
                return "Not Pass";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "Pass";
    }

    public void updateMark() {
        Vector<String> marks = new Vector<String>();
        String query = new String();
        double gpa = 0;
        String status;

        // get data
        query = "SELECT gpa FROM Mark WHERE student_id = ?";
        marks = getData(query, studentId);
        // get all mark of a student
        gpa = inputStudentMark();
        status = checkStatus(gpa);
        // check if the student had marks yet.
        if (marks.size() == 0) {
            query = "INSERT INTO Mark VALUES (?, ?, ?, ?)";
            executeUpdateData(query, classId, studentId, Double.toString(gpa), status);
        } else {
            query = "UPDATE Mark SET gpa = ?, status = ? WHERE student_id = ?";
            executeUpdateData(query, Double.toString(gpa), status, studentId);
        }
        System.out.println("Successful update");
    }

    // main section 
    public void showMenu() {
        // print all available class
        printClasses();
        //input class
        inputClassId();
        //print all student in that class
        printStudents();
        //input student id
        inputStudentId();
        //Update new mark
        updateMark();
    }
}
