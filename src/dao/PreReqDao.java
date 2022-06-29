package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreReqDao {

    private Connection conn;
    private static Logger logger = Logger.getLogger("PreReqDao");

    public PreReqDao(Connection connection) {
        this.conn = connection;
    }

    public void postPreReq(String courseID, String preReqID) throws DBException {

        if(!postedAlready(courseID, preReqID)) {

            String SQL = "INSERT INTO PreReqs (CourseID, PreReqID) Values(?, ?);";

            try (PreparedStatement PS = conn.prepareStatement(SQL)) {

                PS.setString(1, courseID);
                PS.setString(2, preReqID);
                PS.executeUpdate();
                logger.finer("Successful input of pre-req for " + courseID);

            } catch (SQLException exc) {
                exc.printStackTrace();
                logger.log(Level.SEVERE, "Problem posting this preReq", exc);
                logger.exiting("PreReqDao", "postPreReq");
                throw new DBException("Error inserting PreReq: " + preReqID + " " + courseID);
            }
        }

        logger.exiting("PreReqDao", "postPreReq");
    }

    public List<String> getPreReqsFor(String course) throws DBException {

        String SQL = "SELECT PreReqID FROM PreReqs WHERE CourseID = ? ;";

        try(PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, course);
            ResultSet RS = PS.executeQuery();

            List<String> preReqs = new ArrayList<>();
            while (RS.next()) {
                preReqs.add(RS.getString(1));
            }

            logger.exiting("PreReqDao", "getPreReqsFor");
            return preReqs;

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem retrieving the preReq list for " + course, exc);
            logger.exiting("PreReqDao", "postPreReq");
            throw new DBException("Error retrieving " + course + " pre-reqs");
        }
    }

    public List<String> itsAPreReqFor(String courseID) throws DBException {

        String SQL = "SELECT courseID FROM PreReqs WHERE PreReqID = ?;";

        try(PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, courseID);
            ResultSet RS = PS.executeQuery();

            List<String> classes = new ArrayList<>();
            while (RS.next()) {
                classes.add(RS.getString(1));
            }

            logger.exiting("PreReqDao", "itsAPreReqFor");
            return classes;

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem finding what " + courseID + " is a pre-req for", exc);
            logger.exiting("PreReqDao", "postPreReq");
            throw new DBException("Error retrieving what " + courseID + " is a pre-req for");
        }
    }

    public void clearPreReqList() throws DBException {

        String SQL = "DELETE FROM PreReqs;";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.executeUpdate();
            logger.finer("Cleared data for PreReqs");

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem clearing the PreReqs table", exc);
            logger.exiting("PreReqDao", "clearPreReqList");
            throw new DBException("Error clearing the PreReqs table");
        }

        logger.exiting("PreReqDao", "clearPreReqList");
    }

    public void clearPreReqsFor(String courseID) throws DBException {
        String SQL = "DELETE FROM PreReqs WHERE CourseID = ?;";

        try (PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, courseID);

            PS.executeUpdate();
            logger.finer("Cleared pre-reqs for class: " + courseID);

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem clearing pre-reqs for " + courseID, exc);
            logger.exiting("PreReqDao", "clearPreReqsFor");
            throw new DBException("Error clearing pre-reqs for " + courseID);
        }

        logger.exiting("PreReqDao", "deletePreReqsFor");
    }

    private boolean postedAlready(String courseID, String preReqID) throws DBException {

        String SQL = "SELECT * FROM PreReqs WHERE CourseID = ? AND PreReqID = ?;";

        try(PreparedStatement PS = conn.prepareStatement(SQL)) {

            PS.setString(1, courseID);
            PS.setString(2, preReqID);
            ResultSet RS = PS.executeQuery();
            if (RS.next()) {
                logger.fine("Pre Req " + preReqID + " for " + courseID + " found!");
                logger.exiting("PreReqDao", "checkIfInThere");
                return true;
            }
            else {
                logger.finest("Pre Req " + preReqID + " for " + courseID + " not found");
                logger.exiting("PreReqDao", "checkIfInThere");
                return false;
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Problem finding this preReq", exc);
            logger.exiting("PreReqDao", "postPreReq");
            throw new DBException("Error finding if " + courseID + " & " + preReqID + "are in the PreReqs table already");
        }


    }


}
