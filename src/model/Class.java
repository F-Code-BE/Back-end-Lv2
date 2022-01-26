package model;

public class Class {
    private String id;
    private String groupId;
    private String courseId;
    private String semesterId;
    private int totalStudent;

    public Class() {
    }

    public Class(String id, String groupId, String courseId, String semesterId, int totalStudent) {
        this.id = id;
        this.groupId = groupId;
        this.courseId = courseId;
        this.totalStudent = totalStudent;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public int getTotalStudent() {
        return totalStudent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setCourseId(String courseId) {
        this.courseId =courseId;
    }
    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }
    public void setTotalStudent(int totalStudent) {
        this.totalStudent = totalStudent;
    }

    public void increaseStudent(int number) {
        this.totalStudent += number;
    }
}
