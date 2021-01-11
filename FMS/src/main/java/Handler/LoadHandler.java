package Handler;

import Reqeust.LoadRequest;
import Result.LoadResult;
import Service.LoadService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        LoadResult loadResult = new LoadResult();
        HandlerParent hp = new HandlerParent();

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                LoadService loadService = new LoadService();
                Reader reqBody = new InputStreamReader(exchange.getRequestBody());
                Gson gson = new Gson();

                LoadRequest loadRequest = gson.fromJson(reqBody, LoadRequest.class);
                loadResult = loadService.load(loadRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                String jsonString = gson.toJson(loadResult);
                OutputStream respBody = exchange.getResponseBody();
                hp.writeString(jsonString, respBody);
                respBody.close();
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String jsonString = loadResult.getMessage();
                OutputStream respBody = exchange.getResponseBody();
                hp.writeString(jsonString, respBody);
                respBody.close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
