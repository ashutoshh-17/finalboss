import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
    String sendMessage(String msg) throws RemoteException;
}