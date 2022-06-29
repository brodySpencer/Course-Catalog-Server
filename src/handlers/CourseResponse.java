package handlers;

import models.Course;

public class CourseResponse {

    private String message;
    private boolean success;

    private Course course;

    public CourseResponse() {
    }

    public CourseResponse(Course course, boolean success) {
        this.course = course;
        this.success = success;
    }

    public CourseResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
