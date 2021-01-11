package Handler;

import Result.EventResult;
import Result.EventResultSingle;
import Service.EventService;
import Service.EventServiceSingle;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HandlerParent hp = new HandlerParent();
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {

                    String authToken = reqHeaders.getFirst("Authorization");
                    String reqURL = exchange.getRequestURI().toString();
                    String[] parseString = reqURL.split("/");

                    if (parseString.length == 3) {

                        EventServiceSingle eventServiceSingle = new EventServiceSingle();

                        EventResultSingle eventResultSingle = eventServiceSingle.getSingleEvent(parseString[2], authToken);
                        String jsonString = gson.toJson(eventResultSingle);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);

                        OutputStream respBody = exchange.getResponseBody();
                        hp.writeString(jsonString, respBody);
                        respBody.close();
                    }
                    else if (parseString.length == 2) {

                        EventService eventService = new EventService();

                        EventResult eventResult = eventService.getAllEvents(authToken);
                        String jsonString = gson.toJson(eventResult);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);

                        OutputStream respBody = exchange.getResponseBody();
                        hp.writeString(jsonString, respBody);
                        respBody.close();
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                        String jsonString = "{\"message\" : \"Invalid request\"}";
                        OutputStream respBody = exchange.getResponseBody();
                        hp.writeString(jsonString, respBody);
                        respBody.close();
                    }
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    exchange.getResponseBody().close();
                }
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
