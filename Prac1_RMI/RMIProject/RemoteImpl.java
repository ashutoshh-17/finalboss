import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteImpl extends UnicastRemoteObject implements RemoteInterface {

    protected RemoteImpl() throws RemoteException {
        super();
    }

    // This method is automatically handled in separate threads for each client
    public String sendMessage(String msg) throws RemoteException {
        String threadName = Thread.currentThread().getName();
        System.out.println("Received from client: " + msg + " | Thread: " + threadName);
        
        return "Server Response: Message received -> " + msg;
    }
}