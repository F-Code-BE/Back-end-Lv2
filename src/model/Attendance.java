package model;

public class Attendance {
    private String studentId;
    private String slotId;
    private String status;

    public Attendance() {
    };

    public Attendance(String studentId, String slotId, String status) {
        this.studentId = studentId;
        this.slotId = slotId;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
