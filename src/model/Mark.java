package model;

public class Mark {
    private String classId;
    private String studentId;
    private double gpa;
    private String status;

    public Mark() {
    }

    public Mark(String classId, String studentId, double gpa, String status) {
        this.classId = classId;
        this.studentId = studentId;
        this.gpa = gpa;
        this.status = status;
    }

    public String getClassId() {
        return classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public double getGpa() {
        return gpa;
    }

    public String getStatus() {
        return status;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
