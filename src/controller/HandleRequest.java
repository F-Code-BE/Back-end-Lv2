package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import Lib.Validation;
import model.Request;
import model.Timetable;
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
            } else if (request.getType() == 3) {

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
            } else {
                String classId = request.getMessage().substring(request.getMessage().indexOf('=') + 1);
                return "exchange class with student in class " + classId;
            }
        } catch (Exception e) {

        }
        return "";
    }

    private static void markAccept(String requestId) {
        var conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("update request set status = ? where id = ?");
            stmt.setString(1, "Accepted");
            stmt.setString(2, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private static boolean changeClass(String classId, String currentClass, String userId) {

        var conn = Singleton.getInstance();
        String teacherId = "";
        Vector<Timetable> timetable = new Vector<>();
        Vector<String> slotIds = new Vector<>();
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class_student cs JOIN Class c ON cs.class_id = c.id JOIN Slot_type st ON cs.class_id = st.class_id WHERE cs.student_id = ?");
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Timetable TimetableModel = new Timetable();
                TimetableModel.setGroupId(resultSet.getString(1));
                TimetableModel.setCourseId(resultSet.getString(2));
                TimetableModel.setSlotType(resultSet.getString(3));
                TimetableModel.setSemesterId(resultSet.getString(4));
                TimetableModel.setMaxStudent(Integer.parseInt(resultSet.getString(5)));
                timetable.add(TimetableModel);
            }

            statement = conn.prepareStatement("SELECT st.id, st.teacher_id FROM Slot_type st WHERE st.class_id = ?");
            statement.setString(1, classId);
            resultSet = statement.executeQuery();

            // Check is there duplicated?
            while (resultSet.next()) {
                String slotType = new String(resultSet.getString(1));
                for (Timetable time : timetable) {
                    if (time.getSlotType().equals(slotType)) {
                        throw new Exception("Duplicated slot");
                    }
                }

                teacherId = resultSet.getString(2);
            }

            // check if there is enough student
            statement = conn.prepareStatement("SELECT max_student FROM Class WHERE id = ?");
            statement.setString(1, currentClass);
            resultSet = statement.executeQuery();
            resultSet.next();
            int maxStudent = Integer.parseInt(resultSet.getString(1));
            if (maxStudent >= 30) {
                throw new Exception("There are no empty slot");
            }

            // update new class to class table
            statement = conn
                    .prepareStatement("UPDATE Class_student SET class_id = ? WHERE student_id = ? AND class_id = ?");
            statement.setString(1, classId);
            statement.setString(2, userId);
            statement.setString(3, currentClass);
            statement.executeUpdate();
            // Update new slot in attendance table
            // delete all current slot
            statement = conn.prepareStatement("DELETE FROM Attendance WHERE student_id = ? AND slot_id LIKE ?");
            statement.setString(1, userId);
            String regexClassId = currentClass + "%";
            statement.setString(2, regexClassId);
            statement.executeUpdate();

            // select all slot from attendance
            statement = conn.prepareStatement("SELECT id FROM slot WHERE class_id = ? AND teacher_id = ?");
            statement.setString(1, classId);
            statement.setString(2, teacherId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                slotIds.add(resultSet.getString(1));
            }
            // Insert new slot
            for (String slotId : slotIds) {
                statement = conn
                        .prepareStatement("INSERT Attendance (student_id, slot_id, status) VALUES (?, ?, 'NOT YET')");
                statement.setString(1, userId);
                statement.setString(2, slotId);
                statement.executeUpdate();
            }
            // increase max student new class by 1
            statement = conn.prepareStatement("UPDATE Class SET max_student = max_student + 1 WHERE id = ?");
            statement.setString(1, classId);
            statement.executeUpdate();

            // decrease max student old class by 1
            statement = conn.prepareStatement("UPDATE Class SET max_student = max_student - 1 WHERE id = ?");
            statement.setString(1, currentClass);
            statement.executeUpdate();

            System.out.println("Successfully change");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Successfully change");
        return true;
    }

    private static String getCurrentClass(Request request, String subject) {
        var conn = Singleton.getInstance();
        PreparedStatement stmt;
        String currentClass = "";
        try {
            stmt = conn.prepareStatement("select class_id from class_student where student_id = ? and class_id like ?");
            stmt.setString(1, request.getStudentId());
            stmt.setString(2, "%" + subject);
            var rs = stmt.executeQuery();

            while (rs.next())
                currentClass = rs.getString(1);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return currentClass;
    }

    private static void handle(int c) {
        var conn = Singleton.getInstance();
        PreparedStatement stmt;

        var request = requests.get(c);
        try {
            if (request.getType() == 1) {
                if (request.getStudentId() != null) {
                    String message = request.getMessage();
                    message += ',';
                    var attributes = message.split(",");
                    String newName = attributes[1];
                    String newDoB = attributes[2];
                    String newMail = attributes[3];
                    String newMajor = attributes[4];
                    newName = newName.substring(newName.indexOf('=') + 1);
                    newDoB = newDoB.substring(newDoB.indexOf('=') + 1);
                    newMail = newMail.substring(newMail.indexOf('=') + 1);
                    newMajor = newMajor.substring(newMajor.indexOf('=') + 1);
                    if (!newName.equalsIgnoreCase("null")) {
                        stmt = conn.prepareStatement("update student set name = ? where id = ?");
                        stmt.setString(1, newName);
                        stmt.setString(2, request.getStudentId());
                        stmt.executeUpdate();
                    }
                    if (!newDoB.equalsIgnoreCase("null")) {
                        stmt = conn.prepareStatement("update student set date_of_birth = ? where id = ?");
                        stmt.setString(1, newDoB);
                        stmt.setString(2, request.getStudentId());
                        stmt.executeUpdate();
                    }
                    if (!newMail.equalsIgnoreCase("null")) {
                        stmt = conn.prepareStatement("update student set mail = ? where id = ?");
                        stmt.setString(1, newMail);
                        stmt.setString(2, request.getStudentId());
                        stmt.executeUpdate();
                    }
                    if (!newMajor.equalsIgnoreCase("null")) {
                        stmt = conn.prepareStatement("update student set major_id = ? where id = ?");
                        stmt.setString(1, newMajor);
                        stmt.setString(2, request.getStudentId());
                        stmt.executeUpdate();
                    }
                    markAccept(request.getRequestId());
               } else {
                   handleTeacherRequest(c);
               }
            } else if (request.getType() == 2) {
                String courseId = request.getMessage().substring(request.getMessage().indexOf('=') + 1);
                stmt = conn.prepareStatement(
                        "update mark set status = ?, gpa = null where student_id = ? and class_id like ?");
                stmt.setString(1, "RETAKE");
                stmt.setString(2, request.getStudentId());
                stmt.setString(3, "%" + courseId);
                stmt.executeUpdate();
                markAccept(request.getRequestId());
              

            } else if (request.getType() == 3) {
                String slotId = request.getMessage().substring(request.getMessage().indexOf('=') + 1);
                stmt = conn.prepareStatement("update attendance set status = ? where student_id = ? and slot_id = ?");
                stmt.setString(1, "Ignoring");
                stmt.setString(2, request.getStudentId());
                stmt.setString(3, slotId);
                stmt.executeUpdate();
                markAccept(request.getRequestId());
                
            } else {
                String classId = request.getMessage().substring(request.getMessage().indexOf('=') + 1);
                String subject = classId.substring(classId.indexOf('_') + 1);
                String currentClass = getCurrentClass(request, subject);

               
                for (int i = 0; i < requests.size(); i++)
                    if (i != c && requests.get(i).getType() == 4) {
                        var otherRequest = requests.get(i);
                        var otherClassId = otherRequest.getMessage()
                                .substring(otherRequest.getMessage().indexOf('=') + 1);
                        var otherCurrentClass = getCurrentClass(otherRequest, subject);
                        if (classId == otherCurrentClass && currentClass == otherClassId) {
                            if (changeClass(classId, currentClass, request.getStudentId())
                                    && changeClass(otherClassId, otherCurrentClass, otherRequest.getStudentId())) {
                                markAccept(request.getRequestId());
                                markAccept(otherRequest.getRequestId());
                            }
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleTeacherRequest(int c) {
        var conn = Singleton.getInstance();
        PreparedStatement stmt;

        var request = requests.get(c);
        try {
            String message = request.getMessage();
            message += ',';
            var attributes = message.split(",");
            String newName = attributes[1];
            String newMail = attributes[2];
            newName = newName.substring(newName.indexOf('=') + 1);
            newMail = newMail.substring(newMail.indexOf('=') + 1);
            if (!newName.equalsIgnoreCase("null")) {
                stmt = conn.prepareStatement("update teacher set name = ? where id = ?");
                stmt.setString(1, newName);
                stmt.setString(2, request.getTeacherId());
                stmt.executeUpdate();
            }
            if (!newMail.equalsIgnoreCase("null")) {
                stmt = conn.prepareStatement("update teacher set mail = ? where id = ?");
                stmt.setString(1, newMail);
                stmt.setString(2, request.getStudentId());
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void acceptRequest() {
        var choices = new ArrayList<Integer>();
        while (true) {
            int number;
            do {
                number = Validation.inputInt("Enter request number that you want to accept (0 to finish): ");
                if (number < 0 || number > requests.size()) {
                    System.out.println("PLease enter the valid number!");
                } 
            } while (number < 0 || number > requests.size());
          
            if (number == 0)
                break;
            choices.add(number);
        }
        var removedRequest = new ArrayList<String>();
        choices.forEach((c) -> {
            handle(c - 1);
            removedRequest.add(requests.get(c - 1).getRequestId());
        });
        requests.removeIf((request) -> {
            return removedRequest.contains(request.getRequestId());
        });
    }

    private static void markReject(String requestId) {
        var conn = Singleton.getInstance();
        try {
            PreparedStatement stmt = conn.prepareStatement("update request set status = ? where id = ?");
            stmt.setString(1, "Rejected");
            stmt.setString(2, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    private static void rejectRequest() {
        var choices = new ArrayList<Integer>();
        while (true) {
            int number;
            do {
                number = Validation.inputInt("Enter request number that you want to reject (0 to finish): ");
                if (number < 0 || number > requests.size()) {
                    System.out.println("PLease enter the valid number!");
                } 
            } while (number < 0 || number > requests.size());
          
            if (number == 0)
                break;
            choices.add(number);
        }
        choices.forEach((c) -> {
            markReject(requests.get(c - 1).getRequestId());
        });   
    }
    public static void showMenu() {
        getRequestsData();
        System.out.println("\n             List of pending requests: \n");
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getStudentId() != null){
                System.out.println(
                    i + 1 + ". Student " + requests.get(i).getStudentId() + " want to " + showChoice(requests.get(i)));
            } else {
                System.out.println(
                    i + 1 + ". Teacher " + requests.get(i).getTeacherId() + " want to " + showChoice(requests.get(i)));
            }
        }
        acceptRequest();
        System.out.println("\n             List of pending requests: \n");
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getStudentId() != null){
                System.out.println(
                    i + 1 + ". Student " + requests.get(i).getStudentId() + " want to " + showChoice(requests.get(i)));
            } else {
                System.out.println(
                    i + 1 + ". Teacher " + requests.get(i).getTeacherId() + " want to " + showChoice(requests.get(i)));
            }
        }
        rejectRequest();
    }
    public static void main(String[] args) {
        showMenu();
    }
}
