import java.io.*;
import java.net.*;

public class BullyProcess {

    int id;
    int port;
    int[] ports;
    boolean isLeader = false;
    boolean receivedOK = false;

    public BullyProcess(int id, int port, int[] ports) {
        this.id = id;
        this.port = port;
        this.ports = ports;
    }

    public void start() throws Exception {

        ServerSocket server = new ServerSocket(port);
        System.out.println("Process " + id + " started on port " + port);

        // Listener Thread
        new Thread(() -> {
            while (true) {
                try {
                    Socket s = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String msg = in.readLine();

                    if (msg.equals("ELECTION")) {
                        System.out.println("Process " + id + " received ELECTION");

                        // Reply OK
                        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                        out.println("OK");

                        // Start own election
                        startElection();
                    }

                    else if (msg.equals("OK")) {
                        System.out.println("Process " + id + " received OK");
                        receivedOK = true;
                    }

                    else if (msg.startsWith("COORDINATOR")) {
                        int leader = Integer.parseInt(msg.split(":")[1]);
                        System.out.println("Process " + id + " accepts leader: " + leader);
                        isLeader = false;
                    }

                    s.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startElection() {
        try {
            System.out.println("Process " + id + " starts election");

            receivedOK = false;

            // Send ELECTION to higher processes
            for (int i = id + 1; i < ports.length; i++) {
                try {
                    sendMessage(ports[i], "ELECTION");
                } catch (Exception e) {
                    // ignore if process not alive
                }
            }

            // Wait for OK responses
            Thread.sleep(2000);

            if (!receivedOK) {
                becomeLeader();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void becomeLeader() {
        isLeader = true;
        System.out.println("Process " + id + " becomes LEADER");

        // Inform all lower processes
        for (int i = 0; i < ports.length; i++) {
            if (i != id) {
                try {
                    sendMessage(ports[i], "COORDINATOR:" + id);
                } catch (Exception e) {}
            }
        }
    }

    public void sendMessage(int port, String msg) throws Exception {
        Socket s = new Socket("localhost", port);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        out.println(msg);
        s.close();
    }

    public static void main(String[] args) throws Exception {

        int id = Integer.parseInt(args[0]);

        int[] ports = {5001, 5002, 5003, 5004, 5005};

        BullyProcess p = new BullyProcess(id, ports[id], ports);
        p.start();

        // Delay before starting election
        Thread.sleep(3000);

        p.startElection();
    }
}