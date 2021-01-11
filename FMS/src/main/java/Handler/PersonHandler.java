package Handler;

import Result.PersonResult;
import Result.PersonResultSingle;
import Service.PersonService;
import Service.PersonServiceSingle;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {

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
                        //single
                        PersonServiceSingle personServiceSingle = new PersonServiceSingle();

                        PersonResultSingle personResultSingle = personServiceSingle.getPerson(parseString[2], authToken);
                        String jsonString = gson.toJson(personResultSingle);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                        OutputStream respBody = exchange.getResponseBody();
                        hp.writeString(jsonString, respBody);
                        respBody.close();
                    }
                    else if (parseString.length == 2) {
                        PersonService personService = new PersonService();

                        PersonResult personResult = personService.getFamilyMembers(authToken);
                        String jsonString = gson.toJson(personResult);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
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
