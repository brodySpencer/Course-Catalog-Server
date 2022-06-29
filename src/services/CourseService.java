package services;

import dao.CourseDao;
import dao.DBException;
import dao.DataBase;
import dao.PreReqDao;
import models.Course;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseService {

    private static Logger logger = Logger.getLogger("CourseService");

    public CourseService() {

    }

    public boolean postCourse(Course newCourse) throws DBException {

        DataBase db = new DataBase();

        try {
            db.createDBConnection();
            CourseDao courseDao = new CourseDao(db.getConnection());

            logger.entering("CourseDao", "deleteCourse");
            courseDao.deleteCourse(newCourse.getID());
            //if there is already a course there by this ID, delete it so we can update the DB with the new one;

            logger.entering("CourseDao", "putCourse");
            courseDao.putCourse(newCourse);

            db.closeConnection(true);
        } catch (DBException exc) {
            db.closeConnection(false);
            logger.log(Level.SEVERE, exc.getMessage(), exc);
            logger.exiting("CourseService", "postCourse");
            return false;
        }

        logger.entering("CourseService", "postPreReqs");
        boolean success = postPreReqs(newCourse.getID(), newCourse.getPreReqs());

        logger.entering("CourseService", "addToCatalog");
        addToCatalog(newCourse);

        logger.entering("Catalog", "insertCatalog");
        logger.exiting("CourseService", "postCourse");
        return success;
    }

    private boolean postPreReqs(String courseID, List<String> preReqs) {

        PreReqService PRS = new PreReqService();
        try {
            PRS.postPreReq(courseID, preReqs);

        } catch (DBException exc) {
            logger.exiting("CourseService", "postPreReqs");
            return false;
        }

        logger.exiting("CourseService", "postPreReqs");
        return true;

    }

    private void addToCatalog(Course course) throws DBException {

        CatalogService CS = new CatalogService();
        logger.entering("CatalogServie", "addToCatalog");
        CS.addToCatalog(course);
    }

    public Course getCourse(String ID) throws DBException {

        DataBase db = new DataBase();

        try {
            db.createDBConnection();

            CourseDao CDao = new CourseDao(db.getConnection());
            Course course = CDao.pullCourse(ID);

            getPreReqs(course);


            logger.exiting("CourseService", "getCourse");
            db.closeConnection(true);
            return course;

        } catch (DBException exc) {
            db.closeConnection(false);
            logger.log(Level.SEVERE, exc.getMessage(), exc);
            logger.exiting("CourseService", "getCourse");
            return null;
        }

    }

    private void getPreReqs(Course course) throws DBException {

        try {

            PreReqService PRS = new PreReqService();
            PRS.addPreReqs(course);

            logger.exiting("CourseService", "getCourse");

        } catch (DBException exc) {
            logger.log(Level.SEVERE, ("Unable to add the preReq list for " + course.getID()), exc);
            logger.exiting("CourseService", "getCourse");
        }

    }



}
