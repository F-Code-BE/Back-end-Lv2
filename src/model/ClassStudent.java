package model;

public class ClassStudent {
    private String classId;
    private String studentId;

    public ClassStudent() {
    }

    public ClassStudent(String classId, String studentId) {
        this.classId = classId;
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
