package streaming;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Remote Interface for RMI File Transfer Service
public interface FileTransferService extends Remote {
    String createRoom() throws RemoteException;
    boolean joinRoom(String roomCode, String username) throws RemoteException;
    void sendFile(String roomCode, String fileName, byte[] fileData) throws RemoteException;
    byte[] receiveFile(String roomCode) throws RemoteException;
    String getLatestFileName(String roomCode) throws RemoteException;
}

