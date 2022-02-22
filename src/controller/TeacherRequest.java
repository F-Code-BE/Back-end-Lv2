package controller;

import java.util.Vector;

import model.Teacher;
import Lib.Regex;
import Lib.Validation;
import view.FapMenu;

public class TeacherRequest extends Request {
    private Teacher user = new Teacher();

    public TeacherRequest(String id) {
        user.setId(id);
    }

    private void changeInfo() {
        user.setName(Validation.inputString("Enter new name (type null to skip): ", "([\\w|\\s]+)"));
        user.setMail(Validation.inputString("Enter new email (type null to skip): ", Regex.EMAIL_PATTERN + "|null"));
        // save to table 
        sendData("1", null, user.getId(), user.toString2());
        System.out.println("Request Successful");
    }

    private void viewRequest() {
        int count = 1;
        String query = "SELECT type, message, status FROM request WHERE teacher_id = ?";
        Vector< Vector<String> > requests = executeDb2("query", query, user.getId());

        for (Vector<String> request : requests) {
            System.out.println((count++) + " " + request.elementAt(2) + ": Change information | " + request.elementAt(1));
        }
    }

    @Override
    public void showMenu() {
        int choice = 0;
        FapMenu menu = new FapMenu();
        menu.add("Change Information"); 
        menu.add("View request");
        menu.add("Exit");
        do {
            choice = menu.getUserChoice();
            switch (choice) {
                case 1:
                    changeInfo();
                    break;
                case 2:
                    viewRequest();
                    break;
                default:
                    break;
            }
        } while (choice <= 5);
    }
}
