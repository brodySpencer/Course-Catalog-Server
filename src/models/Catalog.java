package models;

public class Catalog {

    private Course[] catalog;
    private boolean success;

    public Catalog(Course[] catalog, boolean success) {
        this.catalog = catalog;
        this.success = success;
    }

    public Course[] getCatalog() {
        return catalog;
    }

    public void setCatalog(Course[] catalog) {
        this.catalog = catalog;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
