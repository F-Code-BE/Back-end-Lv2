package model;

public class MajorCourse {
    private String majorId;
    private String courseId;
    private String coursePrequisite;
    private String alternativeCourse;

    public MajorCourse() {
    }

    public MajorCourse(String majorId, String courseId, String coursePrequisite, String alternativeCourse) {
        this.majorId = majorId;
        this.courseId = courseId;
        this.coursePrequisite = coursePrequisite;
        this.alternativeCourse = alternativeCourse;
    }

    public String getMajorId() {
        return majorId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCoursePrequisite() {
        return coursePrequisite;
    }

    public String getAlternativeCourse() {
        return alternativeCourse;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void CoursePrequisite(String course) {
        this.coursePrequisite = course;
    }

    public void setAlternativeCourse(String course) {
        this.alternativeCourse = course;
    }
}