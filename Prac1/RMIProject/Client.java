import java.rmi.Naming;

class ClientThread extends Thread {
    public void run() {
        try {
            RemoteInterface obj = (RemoteInterface)
                Naming.lookup("rmi://localhost:1099/MyServer");

            String msg = "Hello from " + Thread.currentThread().getName();
            String response = obj.sendMessage(msg);

            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Client {
    public static void main(String[] args) {
        // Create multiple client threads
        for (int i = 0; i < 5; i++) {
            new ClientThread().start();
        }
    }
}