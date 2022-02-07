package controller;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.*;
import Lib.Validation;
import patterns.Singleton;

class TimeTable {
    private String groupId;
    private String slotType;
    private String courseId;
    private String semesterId;
    private int maxStudent;

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSlotType() {
        return this.slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSemesterId() {
        return this.semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public int getMaxStudent() {
        return this.maxStudent;
    }

    public void setMaxStudent(int maxStudent) {
        this.maxStudent = maxStudent;
    }

    public TimeTable() {
    }

    public TimeTable(String groupId, String slotType, String courseId, String semesterId, int maxStudent) {
        this.groupId = groupId;
        this.slotType = slotType;
        this.courseId = courseId;
        this.semesterId = semesterId;
        this.maxStudent = maxStudent;
    }
    @Override
    public String toString() {
        return
            " groupId='" + getGroupId() + "'" +
            ", slotType='" + getSlotType() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", semesterId='" + getSemesterId() + "'" +
            ", maxStudent='" + getMaxStudent() + "'" ;
    }
}  

class GroupCourse extends TimeTable { 

}
public class ChangeGroup {
    public static String inputCourseId() {
        String courseId = new String(Validation.inputString("Enter CourseId: ", ""));
        return courseId;
    }
    public static String inputGroupId() {
        String groupId = new String(Validation.inputString("Enter GroupId: ", ""));
        return groupId;
    }
    public static void getAllCourses(String userId) {
        Connection conn = Singleton.getInstance();        
        System.out.println("--------CHANGE GROUP---------");

        try {
            // show time table
            String courseId = new String();
            String currentGroupId = new String();
            String classId = new String();
            String groupId = new String();
            String oldClass = new String();
            Vector <TimeTable> timetable = new Vector<TimeTable>();
            int count = 0;
            Statement statement = conn.createStatement();
            String queryString = "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class_student cs JOIN Class c ON cs.class_id = c.id JOIN Slot_type st ON cs.class_id = st.class_id WHERE cs.student_id = " + "'" + userId + "'";
            ResultSet resultSet = statement.executeQuery(queryString);
            
            System.out.println("ALL COURSE:");
            while (resultSet.next()) {
                TimeTable timetableModel = new TimeTable();
                timetableModel.setGroupId(resultSet.getString(1));
                timetableModel.setCourseId(resultSet.getString(2));
                timetableModel.setSlotType(resultSet.getString(3));
                timetableModel.setSemesterId(resultSet.getString(4));
                timetableModel.setMaxStudent(Integer.parseInt(resultSet.getString(5))); 
                timetable.add(timetableModel);
            }
            for (TimeTable t : timetable) {
                System.out.println(t.toString());
            }
            courseId = inputCourseId();    
            for (TimeTable t : timetable) {
                if (t.getCourseId().equals(courseId)) {
                    currentGroupId = t.getGroupId();
                    break;
                }
            }
            // show all available classes for the course
            queryString = "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class c JOIN Slot_type st ON c.id = st.class_id WHERE c.course_id = " + "'" + courseId + "'" +  "AND NOT c.group_id = " + "'" + currentGroupId + "'";
            resultSet = statement.executeQuery(queryString);
            Vector<GroupCourse> groupCourse = new Vector<GroupCourse>();
            while(resultSet.next()) {
                GroupCourse groupCourseModel = new GroupCourse();
                groupCourseModel.setGroupId(resultSet.getString(1));
                groupCourseModel.setCourseId(resultSet.getString(2));
                groupCourseModel.setSlotType(resultSet.getString(3));
                groupCourseModel.setSemesterId(resultSet.getString(4));
                groupCourseModel.setMaxStudent(Integer.parseInt(resultSet.getString(5))); 
                groupCourse.add(groupCourseModel);
            }
            
            System.out.println("ALL GROUP AVAILABLE: ");
            for (GroupCourse gr : groupCourse) {
                System.out.println(gr.toString());
            }

            //Choose group id and check if there was a duplicate
            groupId = inputGroupId();
            classId =  groupId + "_" + courseId;
            oldClass = currentGroupId + "_" +courseId;
            queryString = "SELECT st.id FROM Slot_type st WHERE st.class_id = " + "'" + classId + "'";
            resultSet = statement.executeQuery(queryString);

            while(resultSet.next()) {
                String slotType =  new String(resultSet.getString(1));
                for (TimeTable time : timetable) {
                    if (time.getSlotType().equals(slotType)) {
                        throw new Exception("Duplicated slot");
                    }
                }
            }
            
            // update new class to class table
            queryString = "UPDATE Class_student SET class_id = '" + classId + "' WHERE student_id = '" + userId + "' AND class_id = '" + oldClass + "'";
            statement.executeUpdate(queryString);
            System.out.println("Successfully change");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        getAllCourses("SE160001");
    }
}
