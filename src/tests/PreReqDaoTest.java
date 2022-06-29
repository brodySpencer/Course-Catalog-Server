package tests;

import dao.DBException;
import dao.DataBase;
import dao.PreReqDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreReqDaoTest {

    private DataBase db;

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
    public void postPreReqTest() throws DBException {
        PreReqDao pRDao = new PreReqDao(db.getConnection());
        pRDao.postPreReq("ECON 260", "ECON 110");
        pRDao.postPreReq("ECON 260", "MATH 112");
        List<String> preReqs = pRDao.getPreReqsFor("ECON 260");
        assertEquals(2, preReqs.size());

        pRDao.postPreReq("ECON 260", "ECON 110");
        preReqs = pRDao.getPreReqsFor("ECON 260");
        assertEquals(2, preReqs.size());
    }

    @Test
    public void getPreReqsFor() throws DBException {
        PreReqDao pRDao = new PreReqDao(db.getConnection());
        pRDao.postPreReq("ECON 260", "ECON 110");
        pRDao.postPreReq("ECON 260", "MATH 112");
        List<String> preReqs = pRDao.getPreReqsFor("ECON 260");
        assertEquals(2, preReqs.size());

        pRDao.postPreReq("MATH 112", "MATH 110");
        preReqs = pRDao.getPreReqsFor("MATH 112");
        assertEquals("MATH 110", preReqs.get(0));

        pRDao.clearPreReqsFor("ECON 260");
        preReqs = pRDao.getPreReqsFor("ECON 260");
        assertEquals(0, preReqs.size());

        preReqs = pRDao.getPreReqsFor("Random Course");
        assertEquals(0, preReqs.size());
    }

    @Test
    public void itsAPreReqForTest() throws DBException {
        PreReqDao pRDao = new PreReqDao(db.getConnection());
        pRDao.postPreReq("ECON 260", "ECON 110");
        pRDao.postPreReq("ECON 260", "MATH 112");
        pRDao.postPreReq("MATH 113", "MATH 112");
        List<String> leadTos = pRDao.itsAPreReqFor("MATH 112");
        assertEquals("ECON 260", leadTos.get(0));
        assertEquals("MATH 113", leadTos.get(1));

        leadTos = pRDao.itsAPreReqFor("ECON 110");
        assertEquals(1, leadTos.size());

        leadTos = pRDao.itsAPreReqFor("RAND 101");
        assertEquals(0, leadTos.size());
    }

    @Test
    public void clearPreReqListTest() throws DBException {
        PreReqDao pRDao = new PreReqDao(db.getConnection());
        pRDao.postPreReq("ECON 260", "ECON 110");
        pRDao.postPreReq("ECON 260", "MATH 112");
        pRDao.postPreReq("MATH 113", "MATH 112");
        pRDao.postPreReq("HIST 203", "SWELL 190");

        pRDao.clearPreReqList();
        List<String> preReqs = pRDao.getPreReqsFor("ECON 260");
        assertEquals(0, preReqs.size());

    }

    @Test
    public void clearPreReqsForTest() throws DBException {
        PreReqDao pRDao = new PreReqDao(db.getConnection());
        pRDao.postPreReq("ECON 260", "ECON 110");
        pRDao.postPreReq("ECON 260", "MATH 112");
        pRDao.clearPreReqsFor("ECON 260");

        List<String> found = pRDao.getPreReqsFor("ECON 260");
        assertEquals(0, found.size());
    }


}
