package Handler;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Reqeust.FillRequest;
import Result.FillResult;
import Service.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HandlerParent hp = new HandlerParent();
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                FillService fillService = new FillService();

                String reqURL = exchange.getRequestURI().toString();
                String[] parseString = reqURL.split("/");

                if (parseString.length != 4 && parseString.length != 3) {
                    FillResult result = new FillResult();
                    result.setMessage("invalid arguments");
                    String jsonString = gson.toJson(result);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);

                    OutputStream respBody = exchange.getResponseBody();
                    hp.writeString(jsonString, respBody);
                    respBody.close();
                }
                String userName = parseString[2];
                boolean valid = true;

                Database db = new Database();
                try {
                    Connection connection = db.openConnection();
                    UserDAO userDAO = new UserDAO(connection);
                    if (userDAO.findUserByName(userName) == null) {
                        valid = false;
                    }
                    db.closeConnection(true);
                }
                catch (DataAccessException e) {
                    try {
                        db.closeConnection(false);
                    }
                    catch (DataAccessException ex) {
                        throw new IOException("error");
                    }
                }
                if (valid) {
                    int numGeneration = 4;
                    if (parseString.length == 4) {
                        numGeneration = Integer.parseInt(parseString[3]);
                    }
                    FillRequest fillRequest = new FillRequest(userName, numGeneration);
                    FillResult fillResult = fillService.fill(fillRequest);

                    String jsonString = gson.toJson(fillResult);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                    OutputStream respBody = exchange.getResponseBody();
                    hp.writeString(jsonString, respBody);
                    respBody.close();
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
