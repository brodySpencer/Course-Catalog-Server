package services;

import dao.DBException;
import dao.DataBase;
import dao.PreReqDao;
import models.Course;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreReqService {

    private static Logger logger = Logger.getLogger("PreReqService");

    public void postPreReq(String courseID, List<String> preReqList) throws DBException {
        DataBase db = new DataBase();
        try {
            db.createDBConnection();
            PreReqDao PRdao = new PreReqDao(db.getConnection());

            for (String preReq : preReqList) {
                logger.entering("PreReqDao", "postPreReq");
                PRdao.postPreReq(courseID, preReq);
            }

            db.closeConnection(true);
        } catch (DBException exc) {
            db.closeConnection(false);
            logger.log(Level.SEVERE, exc.getMessage(), exc);
            logger.exiting("PreReqService", "postPreReq");
            throw exc;
        }

    }

    public void addPreReqs(Course course) throws DBException {

        DataBase db = new DataBase();
        try {

            db.createDBConnection();
            PreReqDao PRdao = new PreReqDao(db.getConnection());
            course.setPreReqs(PRdao.getPreReqsFor(course.getID()));

            db.closeConnection(true);

        } catch (DBException exc) {
            db.closeConnection(false);
            logger.log(Level.SEVERE, exc.getMessage(), exc);
            logger.exiting("PreReqService", "addPreReqs");
            throw exc;
        }



    }
}
