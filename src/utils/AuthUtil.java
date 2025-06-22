package utils;

import core.Request;
import core.Response;
import core.Main;
import com.sun.net.httpserver.Headers;

public class AuthUtil {

    public static boolean authorizeOrAbort(Request req, Response res) {
        Headers headers = req.getHttpExchange().getRequestHeaders();
        String authHeader = headers.getFirst("Authorization");

        if (authHeader == null || authHeader.isEmpty()) {
            res.setBody("{\"error\":\"Authorization header tidak ditemukan\"}");
            res.send(401);
            return false;
        }

        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (Main.API_KEY.equals(token)) return true;
        }

        if (authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring(6);
            String decoded = new String(java.util.Base64.getDecoder().decode(base64Credentials));

            if (decoded.equals(Main.API_KEY + ":")) return true;
        }

        res.setBody("{\"error\":\"API key tidak valid atau format Authorization salah\"}");
        res.send(401);
        return false;
    }
}
