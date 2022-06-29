package tests;

import dao.CourseDao;
import dao.DBException;
import dao.DataBase;
import models.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDaoTest {

    private DataBase db;
    private Course testCourse;

    @BeforeEach
    public void setUp() throws DBException {
        db = new DataBase();
        db.createDBConnection();
        List<String> preReqs = new ArrayList<>();
        preReqs.add("MATH 100");
        testCourse = new Course("US History", "Covers colonization, revolution, civil war, and the first two world wars",
                "ECON 110", preReqs, 6, "I think I'd like to take this course");
    }

    @AfterEach
    public void tearDown() throws DBException {
        db.closeConnection(false);
    }

    @Test
    public void putCourseTest() throws DBException, SQLException {
        CourseDao cDao = new CourseDao(db.getConnection());
        boolean isClosed = db.getConnection().isClosed();
        cDao.putCourse(testCourse);
        Course found = cDao.pullCourse(testCourse.getID());
        assertEquals(testCourse.getDescription(),found.getDescription());

        Course badCourse = getBadCourse();
        assertThrows(DBException.class, () -> {cDao.putCourse(badCourse);});
    }

    @Test
    public void pullCourseTest() throws DBException {
        CourseDao cDao = new CourseDao(db.getConnection());
        cDao.putCourse(testCourse);
        Course found = cDao.pullCourse(testCourse.getID());
        assertEquals(testCourse.getNotes(), found.getNotes());

        assertNull(cDao.pullCourse("50"));
    }

    @Test
    public void clearAllTest() throws DBException {
        CourseDao cDao = new CourseDao(db.getConnection());
        cDao.putCourse(testCourse);
        Course found = cDao.pullCourse(testCourse.getID());
        assertEquals(testCourse.getDescription(),found.getDescription());

        cDao.clearAllCourses();
        assertNull(cDao.pullCourse(testCourse.getID()));

    }

    @Test
    public void deleteCourseTest() throws DBException {
        CourseDao cDao = new CourseDao(db.getConnection());
        cDao.putCourse(testCourse);
        Course found = cDao.pullCourse(testCourse.getID());
        assertEquals(testCourse.getDescription(),found.getDescription());

        cDao.deleteCourse(testCourse.getID());
        assertNull(cDao.pullCourse(testCourse.getID()));

        cDao.deleteCourse(testCourse.getID());
    }

    private Course getBadCourse() {
        Course badCourse = new Course();
        badCourse.setName("Course With ID same as test course");
        badCourse.setRating(2);
        badCourse.setNotes("");
        badCourse.setID("ECON 110");
        return badCourse;
    }

}
