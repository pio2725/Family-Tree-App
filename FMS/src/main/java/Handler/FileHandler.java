package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

            String url = exchange.getRequestURI().toString();

            if (url.length() == 1 || url == null) {
                String filePath = "C:\\Users\\pio27\\IdeaProjects\\FMS\\web\\index.html";

                File file = new File(filePath);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                Files.copy(file.toPath(), exchange.getResponseBody());
                exchange.getResponseBody().close();
            }
            else {
                String filePath = "web" + url;
                String file404Path = "C:\\Users\\pio27\\IdeaProjects\\FMS\\web\\HTML\\404.html";

                File file = new File(filePath);

                if (!file.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    File wrongFile = new File(file404Path);
                    Files.copy(wrongFile.toPath(), exchange.getResponseBody());
                    exchange.getResponseBody().close();
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(file.toPath(), exchange.getResponseBody());
                    exchange.getResponseBody().close();
                }
            }
        }
        else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().close();
        }
    }
}
