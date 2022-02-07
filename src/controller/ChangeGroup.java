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
    
public class ChangeGroup {
    public static inputCourseId() {
        String courseId = new String();
    }
    public static void getAllCourses(String userId) {
        Connection conn = Singleton.getInstance();        
        System.out.println("-------------------------");

        try {
            TimeTable timetable = new TimeTable();
            Statement statement = conn.createStatement();
            String queryString = "SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class_student cs JOIN Class c ON cs.class_id = c.id JOIN Slot_type st ON cs.class_id = st.class_id WHERE cs.student_id = " + "'" + userId + "'";
            ResultSet resultSet = statement.executeQuery(queryString);
            
            while(resultSet.next()) {    
                System.out.println("true");
                timetable.setGroupId(resultSet.getString(1));
                timetable.setCourseId(resultSet.getString(2));
                timetable.setSlotType(resultSet.getString(3));
                timetable.setSemesterId(resultSet.getString(4));
                timetable.setMaxStudent(Integer.parseInt(resultSet.getString(5)));
                System.out.println(timetable.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void main(String[] args) {
        getAllCourses("SE160001");
    }
}
