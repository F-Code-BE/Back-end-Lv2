package model;

public class Slot {
    private String id;
    private String classId;
    private String teacherId;
    private String roomId;

    public Slot() {
    }

    public Slot(String id, String classId, String teacherId, String roomId) {
        this.id = id;
        this.classId = classId;
        this.teacherId = teacherId;
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public String getClassId() {
        return classId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
