package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


import model.Request;
import patterns.Singleton;

public class HandleRequest {
    private static ArrayList<Request> requests = new ArrayList<>();
    private static void getRequestsData() {
        Connection conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Request");
            var rs = stmt.executeQuery();
            while (rs.next()) {
                String status = rs.getString(6);
                if (!status.equalsIgnoreCase("Pending")) 
                    continue;
                String id = rs.getString(1);
                String studentId = rs.getString(2);
                String teacherId = rs.getString(3);
                byte type = rs.getByte(4);
                String message = rs.getString(5);
                requests.add(new Request(id, studentId, teacherId, type, message, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String showChoice(Request request) {
        Connection conn = Singleton.getInstance();
        PreparedStatement stmt;
        try {
            if (request.getType() == 1) {
                return "change information";
            }
            if (request.getType() == 2) {
                String courseId = request.getMessage().substring(request.getMessage().indexOf('=') + 1);
                String courseName = " ";
                stmt = conn.prepareStatement("select name from course where id = ?");
                stmt.setString(1, courseId);
                var rs = stmt.executeQuery();
                while (rs.next()) 
                    courseName = rs.getString(1);
                return "retake " + courseName + " course";
            } 
            else if (request.getType() == 3) {

                String slotId = request.getMessage().substring(request.getMessage().indexOf('=') + 1);
                String date = " ";
                String classId = "";
                byte slot = 0;
                stmt = conn.prepareStatement("select class_id, date, slot from slot where id = ?");
                stmt.setString(1, slotId);
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    classId = rs.getString(1);
                    date = rs.getString(2);
                    slot = rs.getByte(3);
                }
                return "igore checking attendence date " + date + " slot " + slot + " class " + classId;
            }
            else {
                String classId = request.getMessage().substring(request.getMessage().indexOf('=') + 1);
                return "exchange class with student in class " + classId;
            } 
        } catch (Exception e) {
            
        }
        return "";
    }

    private static void markAccept() {
        System.out.println("Enter request number that you want to accept (0 to finish): ");

    }
    public static void showMenu() {
        getRequestsData();
        for (int i = 0; i < requests.size(); i++) {
            System.out.println(i + 1 + ". Student " +requests.get(i).getStudentId() + " want to " + showChoice(requests.get(i)));
        }
        
    }
}
