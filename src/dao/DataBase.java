package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {

    private Connection conn;

    private static Logger logger = Logger.getLogger("dao.DataBase");


    public Connection createDBConnection() throws DBException {
        String dbName = "db" + File.separator + "CourseCatalogDB.db";
        String connectionURL= "jdbc:sqlite:" + dbName;

        logger.log(Level.FINER, "Connecting to the database");
        logger.entering("dao.DataBase", "createDB");


        try {
            conn = DriverManager.getConnection(connectionURL);
            //conn = c; Connection c = DriverManager.getConnection(connectionURL);
            logger.log(Level.FINER, "ConnectionEstablished");
            conn.setAutoCommit(false);
            logger.fine("Connection complete");
            logger.exiting("Database", "createDB");
            return conn;

        } catch(SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "The connection to the database failed", exc);
            throw new DBException("Unable to access the database");
        }

    }

    public Connection getConnection() throws DBException {
        if (conn == null) {
            return createDBConnection();
        }
        else {
            return conn;
        }
    }

    public void closeConnection(boolean commit) throws DBException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "Trouble closing connection", exc);
            throw new DBException("Error while closing the DB connection");
        }

    }
}
