package dao;

import models.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CatalogDao {

    private static Logger logger = Logger.getLogger("CatalogDao");
    private Connection conn;

    public CatalogDao(Connection conn) {
        this.conn = conn;
    }

    public void addCourse(String ID, String name) throws DBException {

        String SQL = "INSERT INTO Catalog (CourseID, Name) VALUES (?,?);";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {
            PS.setString(1, ID);
            PS.setString(2, name);

            PS.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem inserting a class into the Catalog", exc);
            logger.exiting("CatalogDao", "addCourse");
            throw new DBException("Problem inserting a class into the Catalog");
        }

    }

    public List<Course> getCatalog() throws DBException {

        String SQL = "SELECT * FROM Catalog;";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            ResultSet RS = PS.executeQuery();
            ArrayList<Course> courses = new ArrayList<>();

            while(RS.next()) {
                 Course course = new Course(RS.getString("CourseID"), RS.getString("Name"));
                courses.add(course);
            }

            return courses;

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem retrieving the whole catalog", exc);
            logger.exiting("CatalogDao", "getCatalog");
            throw new DBException("Problem retrieving the whole catalog");
        }
    }

    public void takeOffCourse(String ID) throws DBException {

        String SQL = "DELETE FROM Catalog WHERE CourseID = ?;";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, ID);
            PS.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem deleting an entry in the Catalog", exc);
            logger.exiting("CatalogDao", "takeOffCourse");
            throw new DBException("Problem deleting an entry in the Catalog");
        }

        logger.exiting("CatalogDao", "takeOffCourse");

    }

    public void clearCatalog() throws DBException {

        String SQL = "DELETE FROM Catalog;";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem clearing the Catalog", exc);
            logger.exiting("CatalogDao", "clearCatalog");
            throw new DBException("Problem clearing the Catalog");
        }

        logger.exiting("CatalogDao", "clearCatalog");

    }

}
