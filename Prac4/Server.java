import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Server {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started...");

        List<Socket> clients = new ArrayList<>();
        List<Long> times = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        // Accept 3 clients
        for(int i = 0; i < 3; i++) {
            Socket socket = serverSocket.accept();
            clients.add(socket);
            System.out.println("Client connected");
        }

        // Get time from clients
        for(Socket s : clients) {
            DataInputStream in = new DataInputStream(s.getInputStream());
            long clientTime = in.readLong();
            times.add(clientTime);

            System.out.println("Received client time: " + sdf.format(new Date(clientTime)));
        }

        // Server time
        long serverTime = System.currentTimeMillis();
        times.add(serverTime);

        System.out.println("Server time: " + sdf.format(new Date(serverTime)));

        // Calculate average
        long sum = 0;
        for(long t : times) sum += t;
        long avg = sum / times.size();

        System.out.println("Average Time: " + sdf.format(new Date(avg)));

        // Send adjustment
        for(int i = 0; i < clients.size(); i++) {
            Socket s = clients.get(i);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            long diff = avg - times.get(i);
            out.writeLong(diff);

            System.out.println("Sent adjustment to client " + i + ": " + diff + " ms");
        }

        serverSocket.close();
    }
}