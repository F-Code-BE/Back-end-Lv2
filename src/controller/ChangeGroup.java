package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.*;
import Lib.Validation;
import model.Timetable;
import patterns.Singleton;

class GroupCourse extends Timetable {

}

public class ChangeGroup {

    public static String inputCourseId() {
        return new String(Validation.inputString("Enter CourseId: ", ""));
    }

    public static String inputGroupId() {
        return new String(Validation.inputString("Enter GroupId: ", ""));
    }

    public static String inputTeacherId() {
        return new String(Validation.inputString("Enter TeacherID: ", ""));
    }

    public static void getAllCourses(String userId) {
        Connection conn = Singleton.getInstance();
        System.out.println("--------CHANGE GROUP---------");

        try {
            // show time table
            String courseId;
            String classId;
            String groupId;
            String teacherId = new String();
            String currentClass;
            String currentGroupId = new String();
            Boolean flag = false;
            ArrayList<String> slotIds = new ArrayList<>();
            ArrayList<Timetable> timetable = new ArrayList<>();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class_student cs JOIN Class c ON cs.class_id = c.id JOIN Slot_type st ON cs.class_id = st.class_id WHERE cs.student_id = ?");
            statement.setString(1, userId);
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
            do {
                courseId = inputCourseId();
                for (Timetable t : timetable) {
                    if (t.getCourseId().equals(courseId)) {
                        currentGroupId = t.getGroupId();
                        flag = true;                                                                                  
                    }
                }
                if (Boolean.FALSE.equals(flag)) {
                    System.out.println("There is no courses " + courseId + ". Please try again!");
                }
            } while (Boolean.FALSE.equals(flag));

            // show all available classes for the course
            flag = false;
            statement = conn.prepareStatement(
                    "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class c JOIN Slot_type st ON c.id = st.class_id WHERE c.course_id = ?  AND NOT c.group_id = ?");
            statement.setString(1, courseId);
            statement.setString(2, currentGroupId);
            resultSet = statement.executeQuery();
            ArrayList<GroupCourse> groupCourse = new ArrayList<>();

            while (resultSet.next()) {
                GroupCourse groupCourseModel = new GroupCourse();
                groupCourseModel.setGroupId(resultSet.getString(1));
                groupCourseModel.setCourseId(resultSet.getString(2));
                groupCourseModel.setSlotType(resultSet.getString(3));
                groupCourseModel.setSemesterId(resultSet.getString(4));
                groupCourseModel.setMaxStudent(Integer.parseInt(resultSet.getString(5)));
                groupCourse.add(groupCourseModel);
            }
            if (groupCourse.isEmpty()) {
                throw new Exception("There are no group available for you");
            }
            System.out.println("ALL GROUP AVAILABLE: ");
            for (GroupCourse gr : groupCourse) {
                System.out.println(gr.toString());
            }

            // input group
            do {
                groupId = inputGroupId();
                for (GroupCourse gr : groupCourse) {
                    if (groupId.equals(gr.getGroupId())) {
                        flag = true;
                    }
                }

                if (Boolean.FALSE.equals(flag)) {
                    System.out.println("There are no group " + groupId + ". Please try again!");
                }
            } while (Boolean.FALSE.equals(flag));
            
            // Choose class id and check if there was a duplicate
            classId = groupId + "_" + courseId;
            currentClass = currentGroupId + "_" + courseId;
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
            statement = conn.prepareStatement("UPDATE Class_student SET class_id = ? WHERE student_id = ? AND class_id = ?");
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
        }
    }
}
