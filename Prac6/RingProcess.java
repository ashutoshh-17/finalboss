import java.io.*;
import java.net.*;
import java.util.*;

public class RingProcess {

    int id, myPort, nextPort;

    public RingProcess(int id, int myPort, int nextPort) {
        this.id = id;
        this.myPort = myPort;
        this.nextPort = nextPort;
    }

    public void start() throws Exception {

        ServerSocket server = new ServerSocket(myPort);

        // Connect to next node
        Socket next = null;
        while (next == null) {
            try {
                next = new Socket("localhost", nextPort);
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }

        DataOutputStream out = new DataOutputStream(next.getOutputStream());

        Socket prev = server.accept();
        DataInputStream in = new DataInputStream(prev.getInputStream());

        // Initiator starts election
        if (id == 1) {
            System.out.println("Process 1 starts election");
            out.writeUTF("ELECTION:" + id);
        }

        while (true) {

            String msg = in.readUTF();

            // ----------- ELECTION PHASE -----------
            if (msg.startsWith("ELECTION")) {

                String data = msg.split(":")[1];
                List<Integer> list = new ArrayList<>();

                for (String s : data.split(",")) {
                    list.add(Integer.parseInt(s));
                }

                if (!list.contains(id)) {
                    list.add(id);
                }

                System.out.println("Process " + id + " received ELECTION " + list);

                // If message returns to initiator
                if (list.get(0) == id && list.size() > 1) {

                    int leader = Collections.max(list);
                    System.out.println("Process " + id + " elected Leader: " + leader);

                    out.writeUTF("LEADER:" + leader);
                } else {
                    out.writeUTF("ELECTION:" + list.toString().replaceAll("[\\[\\] ]", ""));
                }
            }

            // ----------- LEADER PHASE -----------
            else if (msg.startsWith("LEADER")) {

                int leader = Integer.parseInt(msg.split(":")[1]);

                System.out.println("Process " + id + " knows Leader is: " + leader);

                // Pass leader message until full ring complete
                if (leader != id) {
                    out.writeUTF(msg);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        int id = Integer.parseInt(args[0]);
        int myPort = Integer.parseInt(args[1]);
        int nextPort = Integer.parseInt(args[2]);

        RingProcess p = new RingProcess(id, myPort, nextPort);
        p.start();
    }
}