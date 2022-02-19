package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.Student;
import view.FapMenu;
import Lib.Regex;
import Lib.Validation;
import patterns.Singleton;

public class StudentRequest {

    private Student user = new Student();
    private String majorId;
    private Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public StudentRequest(String id) {
        user.setId(id);
    }
    // get data using query string 
    public Vector<String> executeDb(String type, String query, String... params) {
        conn = Singleton.getInstance();
        Vector<String> result = new Vector<String>();

        try {
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            if (type.equals("update")) {
                statement.executeUpdate();
                return null;
            } else {
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    result.add(resultSet.getString(1));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public int getRequestId() {
        String query = "SELECT id from Request ORDER BY id DESC";
        Vector<String> ids = executeDb("query", query); 
        if (ids.size() == 0) {
            return 0;
        }
        return Integer.parseInt(ids.firstElement());
    }

    public String getMajorId() {
        String query = "SELECT id FROM major";
        Vector<String> majors = new Vector<String>();
        majors = executeDb("query", query);

        do {
            majorId = Validation.inputString("Enter major id: ", "");
            if (majorId.equals("null")) {
                return null;
            }
            // check if that classid existed
            try {
                if (majors.contains(majorId)) {
                    return majorId;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Invalid Id. Please enter again!");
        } while (true);
    }

    public void sendData(String type, String studentId, String teacherId, String message) {
        int currentId = getRequestId();
        String query = "INSERT INTO Request VALUES (?, ?, ?, ?, ?, ?)";
        executeDb("update", query, Integer.toString(++currentId), studentId, teacherId, type, message, "Pending");
    }

    public void changeInfo() {
        user = new Student();
        user.setId("SE160001");
        user.setName(Validation.inputString("Enter new name (type null to skip): ", "([\\w|\\s]+)"));
        user.setMail(Validation.inputString("Enter new email (type null to skip): ", Regex.EMAIL_PATTERN + "|null"));
        user.setDateOfBirth(Validation.inputDate("Enter new date of birth (dd/mm/yyyyy or null to skip): ", true));
        user.setMajorID(getMajorId());
        // save to table 
        sendData("1", user.getId(), null, user.toString2());
        System.out.println("Request Successful");
    }

    public void retake() {
        user = new Student();
        user.setId("SE160001");
        int choice = 0;
        FapMenu menu = new FapMenu();
        String query = "SELECT c.course_id FROM Class_student cs JOIN class c ON c.id = cs.class_id WHERE student_id = ?";
        Vector<String> courseList = new Vector<String>();
        courseList = executeDb("query", query, user.getId());
        
        System.out.println("choose a course: ");
        for (String course : courseList) {
            menu.add(course);
        }
        do {
            choice = menu.getUserChoice();
            choice--;
            if (choice >= courseList.size()) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= courseList.size());
        sendData("2", user.getId(), null, "couresId=" + courseList.get(choice));
        System.out.println("Request successful");
    }

    public void checkAttendance() {
        user = new Student();
        user.setId("SE160001");
        int choice = 0;
        FapMenu menu = new FapMenu();
        String query = " SELECT slot_id from Attendance";
        Vector <String> slots = new Vector <String>();
        slots = executeDb("query", query);

        System.out.println("Choose slot Id: ");
        slots.forEach( (slot) -> menu.add(slot));
        do {
            choice = menu.getUserChoice();
            choice--;
            if (choice >= slots.size()) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= slots.size());
        sendData("3", user.getId(), null, "slotId=" + slots.get(choice));
        System.out.println("Request successful");
    }

    public void alterClass() {
        user = new Student();
        user.setId("SE160001");
        int choice = 0;
        String courseId;
        String classId;
        FapMenu menu = new FapMenu();
        Vector<String> classes = new Vector<String>();
        Vector<String> courseList = new Vector<String>();

        // get all class are available
        String query = "SELECT c.course_id FROM Class_student cs JOIN class c ON c.id = cs.class_id WHERE student_id = ?";
        courseList = executeDb("query", query, user.getId());
        // input course to show all class
        
        System.out.println("choose a course: ");
        for (String course : courseList) {
            menu.add(course);
        }
        do {
            choice = menu.getUserChoice();
            choice--;
            if (choice >= courseList.size()) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= courseList.size());
        courseId = courseList.get(choice);

        //check all classes
        menu = new FapMenu();
        query = "SELECT c.id, st.id, st.teacher_id FROM Slot_type st JOIN class c ON c.id = st.class_id WHERE c.course_id = ?";
        classes = executeDb("query", query, courseId);
        System.out.println("Choose class id");
        for (String classElement : classes) {
            menu.add(classElement);
        }
        do {
            choice = menu.getUserChoice();
            choice--;
            if (choice >= classes.size()) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= classes.size());
        classId = classes.get(choice);

        sendData("4", user.getId(), null, "classid=" + classId);
        System.out.println("Request successful");

    }
    
    public void showMenu() {
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
                    changeInfo();
                    break;
                case 2:
                    retake();
                    break;
                case 3:
                    checkAttendance();
                    break;
                case 4:
                    alterClass();
                default:
                    break;
            }
        } while (choice <= 4);
    }
}