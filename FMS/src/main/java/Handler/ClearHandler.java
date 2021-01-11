package Handler;

import Result.ClearResult;
import Service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        ClearService clearService = new ClearService();
        Gson gson = new Gson();
        ClearResult clearResult = new ClearResult();
        HandlerParent hp = new HandlerParent();

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                clearResult = clearService.clear();
                String jsonString = gson.toJson(clearResult);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                hp.writeString(jsonString, respBody);
                respBody.close();
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                String jsonString = gson.toJson(clearResult.getMessage());

                OutputStream respBody = exchange.getResponseBody();
                hp.writeString(jsonString, respBody);
                respBody.close();
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
