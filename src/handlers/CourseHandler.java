package handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.DBException;
import models.Course;
import services.CourseService;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseHandler extends Handler implements HttpHandler {

    private Logger logger = Logger.getLogger("CourseHandler");

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CourseResponse response = null;

        try {

            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                response = post(exchange);
            }

            else if(exchange.getRequestMethod().toLowerCase().equals("get")) {
                response = get(exchange);
            }

            else {
                response = new CourseResponse("Bad Request", false);
            }

            if (response.isSuccess()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (IOException exc) {
            exc.printStackTrace();
            logger.log(Level.SEVERE, "IO problem" , exc);

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            response = new CourseResponse("Server Error", false);

        }

        String json = getJsonStr(response);

        OutputStream respBody = exchange.getResponseBody();

        sendOut(json, respBody);
        logger.log(Level.FINER, "Sent out course response");

        respBody.close();

        logger.exiting("CourseHandler", "handle");
    }

    private CourseResponse post(HttpExchange exchange) throws IOException {
        CourseResponse response;

        InputStream requestBody = exchange.getRequestBody();
        String jsonStr = readIn(requestBody);
        Course newCourse = getCourseFromJson(jsonStr);
        CourseService CS = new CourseService();
        try {
            logger.entering("CourseService", "postCourse");
            CS.postCourse(newCourse);
            assert newCourse != null;
            response = new CourseResponse(("Successfully added course: " + newCourse.getID()), true);

        } catch (DBException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), false);
            response = new CourseResponse(ex.getMessage(), false);
        }
        return response;
    }

    private CourseResponse get(HttpExchange exchange) throws IOException {
        CourseResponse response;

        InputStream requestBody = exchange.getRequestBody();
        String jsonStr = readIn(requestBody);
        String iD = getIDFromJson(jsonStr);

        if (iD != null) {
            CourseService CS = new CourseService();
            try {
                logger.entering("CourseService", "getCourse");
                Course course = CS.getCourse(iD);
                response = new CourseResponse(course, true);

            } catch (DBException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), false);
                response = new CourseResponse(ex.getMessage(), false);
            }
        }

        else {
            response = new CourseResponse("Bad course ID in request", false);
        }
        return response;
    }

    private String readIn(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private Course getCourseFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Course course = mapper.readValue(json, Course.class);
            logger.log(Level.FINER, "Converted json to Course object alright");
            return course;
        }
        catch (Exception exc) {
            logger.log(Level.SEVERE, "Problem converting JSON request to Course model object", exc);
            return null;
        }

    }

    private String getIDFromJson(String json) {

        class CourseID {
            private String ID;

            public String getID() {
                return ID;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            CourseID courseIDObj = mapper.readValue(json, CourseID.class);
            logger.log(Level.FINER, "Converted json to Course ID alright");
            return courseIDObj.getID();
        }
        catch (Exception exc) {
            logger.log(Level.SEVERE, "Problem converting JSON request to Course model object", exc);
            return null;
        }
    }

}
