package model;

public class Group {
    private String id;

    public Group() {
    }

    public Group(String id, int capacity) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
