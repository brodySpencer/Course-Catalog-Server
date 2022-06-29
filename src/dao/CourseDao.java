package dao;

import models.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseDao {

    private final Connection conn;
    private static Logger logger = Logger.getLogger("CourseDao");

    public CourseDao(Connection conn) {
        this.conn = conn;
    }

    public void putCourse(Course course) throws DBException {

        String SQL = "INSERT INTO Courses (CourseID, Name, CourseDescription, Rating, Notes) VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, course.getID());
            PS.setString(2, course.getName());
            PS.setString(3, course.getDescription());
            PS.setInt(4, course.getRating());
            PS.setString(5, course.getNotes());

            PS.executeUpdate();
            logger.finer("successful input for course " + course.getID());
        } catch(SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Trouble inputting a new course into the database");
            throw new DBException("Error inputting a course into the database");
        }

        logger.exiting("CourseDao", "putCourse");
    }

    public Course pullCourse(String ID) throws DBException{

        String SQL = "SELECT * FROM Courses WHERE CourseID = ?;";

        try(PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, ID);
            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                logger.finer("Course " + ID + " was found");

                Course c = new Course();
                c.setID(RS.getString("CourseID"));
                c.setName(RS.getString("Name"));
                c.setDescription(RS.getString("CourseDescription"));
                c.setRating(RS.getInt("Rating"));
                c.setNotes(RS.getString("Notes"));
                return c;
            }
            else {
                logger.warning("Query failed to find course " + ID );
                return null;
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, ("Trouble querying for Course " + ID), exc);
            throw new DBException("Error querying for Course");
        }

    }

    public void clearAllCourses() throws DBException {

        String SQL = "DELETE FROM Courses;";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.executeUpdate();
            logger.finer("Cleared data for Courses");

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem clearing the Course table", exc);
            logger.exiting("CourseDao", "clearAllCourses");
            throw new DBException("Error clearing the Course table");
        }

        logger.exiting("CourseDao", "clearAllCourses");
    }

    public void deleteCourse(String courseID) throws DBException {

        String SQL = "DELETE FROM Courses WHERE CourseID = ?;";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, courseID);

            PS.executeUpdate();
            logger.finer("Cleared data for class: " + courseID);

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem clearing " + courseID + " from the Course table", exc);
            logger.exiting("CourseDao", "deleteCourse");
            throw new DBException("Error clearing " + courseID + " from the Course table");
        }

        logger.exiting("CourseDao", "deleteCourse");
    }
}
