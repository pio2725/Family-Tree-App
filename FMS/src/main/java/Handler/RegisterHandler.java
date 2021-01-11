package Handler;

import Reqeust.RegisterRequest;
import Result.RegisterResult;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {

    public RegisterHandler() { }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        RegisterResult registerResult = new RegisterResult();
        HandlerParent hp = new HandlerParent();

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                RegisterService registerService = new RegisterService();
                Gson gson = new Gson();
                Reader reqBody = new InputStreamReader(exchange.getRequestBody());

                RegisterRequest registerRequest = gson.fromJson(reqBody, RegisterRequest.class);
                registerResult = registerService.register(registerRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String jsonString = gson.toJson(registerResult);
                OutputStream respBody = exchange.getResponseBody();
                hp.writeString(jsonString, respBody);
                respBody.close();
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String jsonString = registerResult.getMessage();
                OutputStream respBody = exchange.getResponseBody();
                hp.writeString(jsonString, respBody);
                respBody.close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
