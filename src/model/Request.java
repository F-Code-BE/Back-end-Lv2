package model;

import java.sql.Date;

public class Request {
    private String requestId;
    private String studentId;
    private String teacherId;
    private byte type;
    private String message;
    private String status;

    public Request() {
    }

    public Request(String requestId, String studentId, String teacherId, byte type, String message, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.type = type;
        this.message = message;
        this.status = status;
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

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
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

    public void setMessage(String message) {
        this.message = message;
    } 

    public void setStatus(String status) {
        this.status = status;
        
    }
}
