package streaming;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Remote Interface for RMI File Transfer Service
public interface FileTransferService extends Remote {
    // Room Management (Vikram's implementation)
    void createRoom(String roomCode) throws RemoteException;
    void joinRoom(String roomCode) throws RemoteException;

    // File Streaming (Afnan's implementation)
    void uploadFile(String roomCode, byte[] fileData, String fileName) throws RemoteException;
    byte[] downloadFile(String roomCode) throws RemoteException;
}
