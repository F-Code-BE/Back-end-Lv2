package model;

public class Slot {
    private String id;
    private byte slot;
    // TODO: create fields date
    // private Date date;
    private String classId;
    private String teacherId;
    private String roomId;

    public Slot() {
    }

    public Slot(String id, byte slot, String classId, String teacherId, String roomId) {
        this.id = id;
        this.classId = classId;
        this.teacherId = teacherId;
        this.roomId = roomId;
        this.slot = slot;
    }

    public String getId() {
        return id;
    }
    public byte getSlot() {
        return slot;
    }
    // public String getDate() {
    //     return date.toString();
    // }
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

    public void setSlotId(byte slot) {
        this.slot = slot;
    }
    // public void setDate(String date) {
    //     this.date = new Date(date);
    // }
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
