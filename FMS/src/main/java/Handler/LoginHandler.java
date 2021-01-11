package Handler;

import Reqeust.LoginRequest;
import Result.LoginResult;
import Service.LoginService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        LoginResult loginResult = new LoginResult();
        HandlerParent hp = new HandlerParent();

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                LoginService loginService = new LoginService();
                Reader reqBody = new InputStreamReader(exchange.getRequestBody());

                Gson gson = new Gson();

                LoginRequest loginRequest = gson.fromJson(reqBody, LoginRequest.class);
                loginResult = loginService.login(loginRequest);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                String jsonString = gson.toJson(loginResult);
                OutputStream respBody = exchange.getResponseBody();
                hp.writeString(jsonString, respBody);
                respBody.close();
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String jsonString = loginResult.getMessage();
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
