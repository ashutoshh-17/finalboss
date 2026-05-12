import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/add", new MyHandler());
        server.setExecutor(null);

        System.out.println("Server started at http://localhost:8000");
        server.start();
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            String query = t.getRequestURI().getQuery(); // a=10&b=5
            String[] params = query.split("&");

            int a = Integer.parseInt(params[0].split("=")[1]);
            int b = Integer.parseInt(params[1].split("=")[1]);

            int result = a + b;

            String response = "Sum = " + result;

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}