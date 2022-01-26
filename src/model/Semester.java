package model;

public class Semester {
    private String id;
    private int year;

    public Semester() {
    }

    public Semester(String id, int year) {
        this.id = id;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }
}