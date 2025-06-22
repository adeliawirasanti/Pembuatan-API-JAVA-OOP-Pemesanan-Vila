package routes;

import com.sun.net.httpserver.HttpExchange;
import core.Request;
import core.Response;
import controllers.VoucherController;

public class VoucherRoutes {
    public static void handle(HttpExchange httpExchange) {
        Request req = new Request(httpExchange);
        Response res = new Response(httpExchange);
        String method = req.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    handleGet(req, res, path);
                    break;

                case "POST":
                    if (path.equals("/vouchers")) {
                        VoucherController.createVoucher(req, res);
                    } else {
                        res.setBody("{\"error\":\"Endpoint POST tidak ditemukan\"}");
                        res.send(404);
                    }
                    break;

                case "PUT":
                    if (path.matches("^/vouchers/\\d+$")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        VoucherController.updateVoucher(req, res, id);
                    } else {
                        res.setBody("{\"error\":\"Endpoint PUT tidak ditemukan\"}");
                        res.send(404);
                    }
                    break;

                case "DELETE":
                    if (path.matches("^/vouchers/\\d+$")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        VoucherController.deleteVoucher(req, res, id);
                    } else {
                        res.setBody("{\"error\":\"Endpoint DELETE tidak ditemukan\"}");
                        res.send(404);
                    }
                    break;

                default:
                    res.setBody("{\"error\":\"Metode tidak diizinkan\"}");
                    res.send(405);
                    break;
            }
        } catch (Exception e) {
            res.setBody("{\"error\":\"Terjadi kesalahan pada server\"}");
            res.send(500);
        }
    }

    private static void handleGet(Request req, Response res, String path) {
        try {
            if (path.equals("/vouchers")) {
                VoucherController.getAllVouchers(req, res);
                return;
            }

            if (path.matches("^/vouchers/\\d+$")) {
                int id = Integer.parseInt(path.split("/")[2]);
                VoucherController.getVoucherById(req, res, id);
                return;
            }

            res.setBody("{\"error\":\"Endpoint GET tidak ditemukan\"}");
            res.send(404);
        } catch (Exception e) {
            res.setBody("{\"error\":\"Terjadi kesalahan saat memproses permintaan\"}");
            res.send(500);
        }
    }
}
