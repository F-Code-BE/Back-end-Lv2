package model;

public class Timetable {
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

    public Timetable() {
    }

    public Timetable(String groupId, String slotType, String courseId, String semesterId, int maxStudent) {
        this.groupId = groupId;
        this.slotType = slotType;
        this.courseId = courseId;
        this.semesterId = semesterId;
        this.maxStudent = maxStudent;
    }

    @Override
    public String toString() {
        return " groupId='" + getGroupId() + "'"
                + ", slotType='" + getSlotType() + "'"
                + ", courseId='" + getCourseId() + "'"
                + ", semesterId='" + getSemesterId() + "'"
                + ", maxStudent='" + getMaxStudent() + "'";
    }
}
