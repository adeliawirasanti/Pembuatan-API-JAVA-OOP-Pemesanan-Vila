package routes;

import com.sun.net.httpserver.HttpExchange;
import controllers.CustomerController;
import core.Request;
import core.Response;

public class CustomerRoutes {
    public static void handle(HttpExchange exchange) {
        Request req = new Request(exchange);
        Response res = new Response(exchange);
        String method = req.getRequestMethod();
        String path = req.getPath();

        try {
            switch (method) {
                case "GET":
                    handleGet(req, res, path);
                    break;
                case "POST":
                    handlePost(req, res, path);
                    break;
                case "PUT":
                    handlePut(req, res, path);
                    break;
                case "DELETE":
                    handleDelete(req, res, path);
                    break;
                default:
                    res.setBody("{\"error\":\"Metode tidak diizinkan\"}");
                    res.send(405);
            }
        } catch (Exception e) {
            res.setBody("{\"error\":\"Terjadi kesalahan pada server\"}");
            res.send(500);
        }
    }

    private static void handleGet(Request req, Response res, String path) {
        if (path.equals("/customers")) {
            CustomerController.getAllCustomers(req, res);
        } else if (path.matches("^/customers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.getCustomerById(req, res, id);
        } else {
            res.setBody("{\"error\":\"Endpoint GET tidak ditemukan\"}");
            res.send(404);
        }
    }

    private static void handlePost(Request req, Response res, String path) {
        if (path.equals("/customers")) {
            CustomerController.createCustomer(req, res);
        } else {
            res.setBody("{\"error\":\"Endpoint POST tidak ditemukan\"}");
            res.send(404);
        }
    }

    private static void handlePut(Request req, Response res, String path) {
        if (path.matches("^/customers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.updateCustomer(req, res, id);
        } else {
            res.setBody("{\"error\":\"Endpoint PUT tidak ditemukan\"}");
            res.send(404);
        }
    }

    private static void handleDelete(Request req, Response res, String path) {
        if (path.matches("^/customers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.deleteCustomer(req, res, id);
        } else {
            res.setBody("{\"error\":\"Endpoint DELETE tidak ditemukan\"}");
            res.send(404);
        }
    }
}
