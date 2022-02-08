package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
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
            String classId = new String();
            String groupId = new String();
            String currentClass = new String();
            String currentGroupId = new String();
            Boolean flag = false;
            Vector <TimeTable> timetable = new Vector<TimeTable>();
            PreparedStatement statement = conn.prepareStatement("SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class_student cs JOIN Class c ON cs.class_id = c.id JOIN Slot_type st ON cs.class_id = st.class_id WHERE cs.student_id = ?");
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            
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
            do {
                courseId = inputCourseId();
                for (TimeTable t : timetable) {
                    if (t.getCourseId().equals(courseId)) {
                        currentGroupId = t.getGroupId();
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    System.out.println("There is no courses " + courseId + ". Please try again!");
                }    
            } while (!flag);

            // show all available classes for the course
            statement = conn.prepareStatement("SELECT c.group_id, c.course_id, st.id AS slot_type, c.semester_id, c.max_student FROM Class c JOIN Slot_type st ON c.id = st.class_id WHERE c.course_id = ?  AND NOT c.group_id = ?");
            statement.setString(1, courseId);
            statement.setString(2, currentGroupId);
            resultSet = statement.executeQuery();
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
            if (groupCourse.size() == 0) {
                throw new Exception("There are no group available for you");
            }
            System.out.println("ALL GROUP AVAILABLE: ");
            for (GroupCourse gr : groupCourse) {
                System.out.println(gr.toString());
            }

            //Choose group id and check if there was a duplicate
            classId =  groupId + "_" + courseId;
            currentClass = currentGroupId + "_" +courseId;
            statement = conn.prepareStatement("SELECT st.id FROM Slot_type st WHERE st.class_id = ?");
            statement.setString(1, classId);
            resultSet = statement.executeQuery();
            flag = false;

            // input group 
            do {
                groupId = inputGroupId();
                for (TimeTable time : timetable) {
                    if (groupId.equals(time.getGroupId())) {
                        flag = true;
                    }
                }

                if (!flag) {
                    System.out.println("There are no group " + groupId + ". Please try again!");
                }
            } while (!flag);

            //Check is there duplicated? 
            while(resultSet.next()) {
                String slotType =  new String(resultSet.getString(1));
                for (TimeTable time : timetable) {
                    if (time.getSlotType().equals(slotType)) {
                        throw new Exception("Duplicated slot");
                    }
                }
            }
            
            // update new class to class table
            statement = conn.prepareStatement("UPDATE Class_student SET class_id = ? WHERE student_id = ? AND class_id = ?");
            statement.setString(1, classId);
            statement.setString(2, userId);
            statement.setString(3, currentClass);
            statement.executeUpdate();
            System.out.println("Successfully change");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        getAllCourses("SE160001");
    }
}
