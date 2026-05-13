import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5000);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        // Simulated local time (random offset)
        long localTime = System.currentTimeMillis() + (long)(Math.random() * 10000);

        System.out.println("Client local time: " + sdf.format(new Date(localTime)));

        // Send time
        out.writeLong(localTime);

        // Receive adjustment
        long adjustment = in.readLong();

        long newTime = localTime + adjustment;

        System.out.println("Adjustment received: " + adjustment + " ms");
        System.out.println("Adjusted time: " + sdf.format(new Date(newTime)));

        socket.close();
    }
}