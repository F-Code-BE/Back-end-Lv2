package model;

public class Teacher {

    private String id;
    private String name;
    private String mail;

    public Teacher() {
    }

    public Teacher(String id, String name, String mail) {
        this.id = id;
        this.name = name;
        this.mail = mail;
    }

    // set private fields
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    // set private fields
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {

        this.mail = mail;
    }
}
