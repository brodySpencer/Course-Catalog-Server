package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.DBException;
import models.Catalog;
import models.Course;
import services.CatalogService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CatalogHandler extends Handler implements HttpHandler {

    private static final Logger logger = Logger.getLogger("CatalogHandler");
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Catalog catalog = null;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                CatalogService catService = new CatalogService();
                try {
                    catalog = catService.getCatalog();

                } catch (DBException exc) {
                    logger.log(Level.SEVERE, exc.getMessage(), exc);
                    catalog = createBadCatalog();
                }

                if (catalog.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
                }
            }
            else {
                catalog = createBadCatalog();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

        } catch (IOException exc) {
            logger.log(Level.SEVERE, "IO problem", exc);
            catalog = createBadCatalog();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
        }

        String json = getJsonStr(catalog);
        OutputStream respBody = exchange.getResponseBody();
        sendOut(json, respBody);
        logger.log(Level.FINER, "Sent out catalog response");
        respBody.close();
        logger.exiting("CatalogHandler", "handle");
    }

    private Catalog createBadCatalog() {
        Course[] courses = new Course[0];
        return new Catalog(courses, false);
    }
}
