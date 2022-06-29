package models;

import java.util.List;

public class Course {

    private String ID;
    private String name;
    private String description;
    private List<String> preReqs;
    private int rating;
    private String notes;

    public Course() {}

    public Course(String name, String description, String ID, List<String> preReqs, int rating, String notes) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.preReqs = preReqs;
        this.rating = rating;
        this.notes = notes;
    }

    public Course(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<String> getPreReqs() {
        return preReqs;
    }

    public void setPreReqs(List<String> preReqs) {
        this.preReqs = preReqs;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
