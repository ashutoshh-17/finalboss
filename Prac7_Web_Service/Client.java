import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {

        URL url = new URL("http://localhost:8000/add?a=10&b=5");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Response from server: " + inputLine);
        }

        in.close();
    }
}