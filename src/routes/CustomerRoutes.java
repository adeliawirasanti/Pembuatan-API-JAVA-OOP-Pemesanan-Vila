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
        String[] parts = path.split("/");

        try {
            if (parts.length == 2 && parts[1].equals("customers")) {
                if (method.equals("GET")) {
                    CustomerController.getAllCustomers(req, res);
                } else if (method.equals("POST")) {
                    CustomerController.createCustomer(req, res);
                } else {
                    res.setBody("{\"error\":\"Metode tidak diizinkan: " + method + "\"}");
                    res.send(405);
                }
                return;
            }

            if (parts.length == 3 && parts[1].equals("customers")) {
                int customerId = Integer.parseInt(parts[2]);

                switch (method) {
                    case "GET":
                        CustomerController.getCustomerById(req, res, customerId);
                        return;
                    case "PUT":
                        CustomerController.updateCustomer(req, res, customerId);
                        return;
                    case "DELETE":
                        CustomerController.deleteCustomer(req, res, customerId);
                        return;
                    default:
                        res.setBody("{\"error\":\"Metode tidak diizinkan: " + method + "\"}");
                        res.send(405);
                        return;
                }
            }

            res.setBody("{\"error\":\"Endpoint customer tidak ditemukan.\"}");
            res.send(404);

        } catch (NumberFormatException e) {
            res.setBody("{\"error\":\"ID customer harus berupa angka.\"}");
            res.send(400);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Terjadi kesalahan pada server.\"}");
            res.send(500);
        }
    }
}
