package handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;

public class Handler {

    protected void sendOut(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    protected String getJsonStr(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException exc) {
            //logger.log(Level.SEVERE, "Unable to create json string to send back", exc);
            return "{ \"message\": \"Error with from json\", \"success\": \"false\" }";
        }
    }
}
