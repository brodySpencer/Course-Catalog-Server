package tests;

import dao.CatalogDao;
import dao.DBException;
import dao.DataBase;
import models.Catalog;
import models.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatalogDaoTest {

    private DataBase db;
    private Catalog testCat;

    private Course testCourse1 = new Course("US History", "Covers colonization, revolution, civil war, and the first two world wars",
                                    "HIST 100", null, 6, "I think I'd like to take this course");
    private Course testCourse2 = new Course("US History 2", "Covers post-WW2",
            "HIST 210", null, 8, "I think I'd like to take this course this coming fall");

    private Course testCourse3 = new Course("Old History", "Mesopotamia and them ages",
            "HIST 255", null, 2, "Interesting but not worth it to me");
    private Course testCourse4 = new Course("European History", "Middle ages, renassaince, art/lifestyle",
            "HIST 313", null, 4, "I don't think I'll take it, but if I need one");

    @BeforeEach
    public void setUp() throws DBException {
        db = new DataBase();
        db.createDBConnection();

    }

    @AfterEach
    public void tearDown() throws DBException {
        db.closeConnection(false);
    }

    @Test
    public void addCourseTest() throws DBException {
        CatalogDao catDao = new CatalogDao(db.getConnection());
        catDao.addCourse(testCourse1.getID(), testCourse1.getName());

        List<Course> courseList = catDao.getCatalog();
        assertEquals(1, courseList.size());

        catDao.addCourse(testCourse2.getID(), testCourse2.getName());
        catDao.addCourse(testCourse3.getID(), testCourse3.getName());
        courseList = catDao.getCatalog();
        assertEquals(3, courseList.size());

        catDao.addCourse(testCourse1.getID(), testCourse1.getName());
        assertEquals(3, courseList.size());

    }

    @Test
    public void getCatalogTest() throws DBException {
        CatalogDao catDao = new CatalogDao(db.getConnection());
        catDao.addCourse(testCourse1.getID(), testCourse1.getName());
        catDao.addCourse(testCourse2.getID(), testCourse2.getName());
        catDao.addCourse(testCourse3.getID(), testCourse3.getName());
        catDao.addCourse(testCourse4.getID(), testCourse4.getName());

        List<Course> courseList = catDao.getCatalog();
        assertEquals(4, courseList.size());
        assertEquals(courseList.get(2).getName(), testCourse3.getName());

        catDao.takeOffCourse(testCourse3.getID());
        courseList = catDao.getCatalog();
        assertEquals(3, courseList.size());
    }

    @Test
    public void takeOffCourseTest() throws DBException {
        CatalogDao catDao = new CatalogDao(db.getConnection());
        catDao.addCourse(testCourse1.getID(), testCourse1.getName());
        List<Course> courseList = catDao.getCatalog();
        assertEquals(1, courseList.size());
        catDao.takeOffCourse(testCourse1.getID());
        courseList = catDao.getCatalog();
        assertEquals(0, courseList.size());

        catDao.takeOffCourse(testCourse1.getID());
    }

    @Test
    public void clearCatalogTest() throws DBException {
        CatalogDao catDao = new CatalogDao(db.getConnection());
        catDao.addCourse(testCourse1.getID(), testCourse1.getName());
        catDao.addCourse(testCourse2.getID(), testCourse2.getName());
        catDao.addCourse(testCourse3.getID(), testCourse3.getName());
        catDao.addCourse(testCourse4.getID(), testCourse4.getName());

        List<Course> courseList = catDao.getCatalog();
        assertEquals(4, courseList.size());

        catDao.clearCatalog();
        courseList = catDao.getCatalog();
        assertEquals(0, courseList.size());
    }

}
