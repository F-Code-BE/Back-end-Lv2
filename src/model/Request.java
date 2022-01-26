package model;

import java.sql.Date;

public class Request {
    private String requestId;
    private String studentId;
    private String teacherId;
    private byte type;
    private Date date;

    public Request() {
    }

    public Request(String requestId, String studentId, String teacherId, byte type, Date date) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.type = type;
        this.date = date;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public byte getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
