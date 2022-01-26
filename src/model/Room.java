package model;

public class Room {
    private String id;
    private int capacity;

    public Room() {
    }

    public Room(String id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void increaseCapacity(int number) {
        this.capacity += number;
    }
}