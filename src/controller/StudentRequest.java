package controller;

import java.util.Vector;

import model.Student;
import view.FapMenu;
import Lib.Regex;
import Lib.Validation;

public class StudentRequest extends Request {

    private Student user = new Student();
    private String majorId;

    public StudentRequest(String id) {
        user.setId(id);
    }

    private String getMajorId() {
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

    private void changeInfo() {
        user.setName(Validation.inputString("Enter new name (type null to skip): ", "([\\w|\\s]+)"));
        user.setMail(Validation.inputString("Enter new email (type null to skip): ", Regex.EMAIL_PATTERN + "|null"));
        user.setDateOfBirth(Validation.inputDate("Enter new date of birth (dd/mm/yyyy or null to skip): ", true));
        user.setMajorID(getMajorId());
        // save to table 
        sendData("1", user.getId(), null, user.toString2());
        System.out.println("Request Successful");
    }

    private void retake() {
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
            if (choice >= courseList.size() || choice < 0) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= courseList.size() || choice < 0);
        sendData("2", user.getId(), null, "coursesId=" + courseList.get(choice));
        System.out.println("Request successful");
    }

    private void checkAttendance() {
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
            if (choice >= slots.size() || choice < 0) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= slots.size() || choice < 0);
        sendData("3", user.getId(), null, "slotId=" + slots.get(choice));
        System.out.println("Request successful");
    }               

    private void alterClass() {
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
            if (choice >= courseList.size() || choice < 0) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= courseList.size() || choice < 0);
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
            if (choice >= classes.size() || choice < 0) {
                System.out.println("Wrong input, please try again!");
            }
        } while (choice >= classes.size() || choice < 0);
        classId = classes.get(choice);

        sendData("4", user.getId(), null, "classId=" + classId);
        System.out.println("Request successful");

    }
    private void viewRequest() {
        int count = 1;
        String query = "SELECT type, message, status FROM request WHERE student_Id = ?";
        Vector< Vector<String> > requests = executeDb2("query", query, user.getId());

        for (Vector<String> request : requests) {
            int type = Integer.parseInt(request.elementAt(0));

            switch (type) {
                case 1:                
                    System.out.println((count++) + " " + request.elementAt(2) + ": Change information | " + request.elementAt(1));
                    break;
                case 2:
                    System.out.println((count++) + " " + request.elementAt(2) + ": Retake | " + request.elementAt(1));
                    break;
                case 3:
                    System.out.println((count++) + " " + request.elementAt(2) + ": Check attendance | " + request.elementAt(1));
                    break;
                case 4:
                    System.out.println((count++) + " " + request.elementAt(2) + ": Alternating Class | " + request.elementAt(1));
                    break;
                default:
                    break;
            }
        };
    }
    
    @Override
    public void showMenu() {
        int choice = 0;
        FapMenu menu = new FapMenu();
        menu.add("Change Information"); 
        menu.add("Retake");
        menu.add("Check Attendance");  
        menu.add("Alternating class");
        menu.add("View request");
        menu.add("Exit");
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
                    break;
                case 5:
                    viewRequest();
                    break;
                default:
                    break;
            }
        } while (choice <= 5);
    }
}