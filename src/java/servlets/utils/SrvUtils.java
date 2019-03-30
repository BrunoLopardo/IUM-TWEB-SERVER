package servlets.utils;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import json.JSONManager;

public final class SrvUtils {
    
    private SrvUtils() {}
    
    public static <T> void sendJsonObject(HttpServletResponse response, T obj) {
        sendResponse(response, new JSONManager().serializeJson(obj));
    }
    
    public static void sendStatusCode(HttpServletResponse response, int code) {
        sendResponse(response, "{ \"statusCode\": " + code + " }");
    }
    
    public static void sendResponse(HttpServletResponse response, String res) {
        response.setContentType("application/json");
        response.setStatus(200);
        
        try {
            response.getWriter().println(res);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
