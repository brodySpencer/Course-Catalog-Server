package services;

import dao.CatalogDao;
import dao.DBException;
import dao.DataBase;
import models.Catalog;
import models.Course;

import java.util.List;
import java.util.logging.Logger;

public class CatalogService {

    private static final Logger logger = Logger.getLogger("CatalogService");

    public boolean addToCatalog(Course course) throws DBException {

        DataBase db = new DataBase();

        try {
            db.createDBConnection();

            CatalogDao CatDao = new CatalogDao(db.getConnection());
            CatDao.addCourse(course.getID(), course.getName());

            logger.exiting("CatalogService", "addToCatalog");
            db.closeConnection(true);
            return true;

        } catch (DBException e) {
            db.closeConnection(false);
            logger.exiting("CatalogService", "addToCatalog");
            return false;
        }
    }

    public Catalog getCatalog() throws DBException {

        DataBase db = new DataBase();

        try {
            db.createDBConnection();

            CatalogDao CatDao = new CatalogDao(db.getConnection());
            List<Course> courses = CatDao.getCatalog();
            db.closeConnection(true);

            Course[] courseArray = new Course[courses.size()];
            courses.toArray(courseArray);

            Catalog catalog = new Catalog(courseArray, true);

            logger.exiting("CatalogService", "getCatalog");
            return catalog;


        } catch (DBException e) {
            db.closeConnection(false);
            logger.exiting("CatalogService", "getCatalog");
            return null;
        }
    }

    public void clearCatalog() throws DBException {

        DataBase db = new DataBase();

        try {
            db.createDBConnection();

            CatalogDao CatDao = new CatalogDao(db.getConnection());
            logger.entering("CatalogDao", "clearCatalog");
            CatDao.clearCatalog();

            logger.exiting("CatalogService", "clearCatalog");
            db.closeConnection(true);

        } catch (DBException e) {
            db.closeConnection(false);
            logger.exiting("CatalogService", "clearCatalog");
        }
    }

    public void deleteCourse(Course course) throws DBException {

        DataBase db = new DataBase();

        try {
            db.createDBConnection();

            CatalogDao CatDao = new CatalogDao(db.getConnection());
            logger.entering("CatalogDao", "takeOffCourse");
            CatDao.takeOffCourse(course.getID());

            logger.exiting("CatalogService", "deleteCourse");
            db.closeConnection(true);

        } catch (DBException e) {
            db.closeConnection(false);
            logger.exiting("CatalogService", "deleteCourse");
        }
    }
}
