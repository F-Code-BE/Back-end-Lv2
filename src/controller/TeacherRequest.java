package controller;

import model.Teacher;
import Lib.Regex;
import Lib.Validation;

public class TeacherRequest extends Request {
    private Teacher user = new Teacher();

    public TeacherRequest(String id) {
        user.setId(id);
    }

    public void changeInfo() {
        user.setName(Validation.inputString("Enter new name (type null to skip): ", "([\\w|\\s]+)"));
        user.setMail(Validation.inputString("Enter new email (type null to skip): ", Regex.EMAIL_PATTERN + "|null"));
        // save to table 
        sendData("1", null, user.getId(), user.toString2());
        System.out.println("Request Successful");
    }

    public static void main(String[] args) {
        TeacherRequest test = new TeacherRequest("Phuoc01");
        test.changeInfo();
    }
}
