import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            // Create RMI registry on port 1099
            

            RemoteInterface obj = new RemoteImpl();

            // Bind object to name
            Naming.rebind("rmi://localhost:1099/MyServer", obj);

            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}