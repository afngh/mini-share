package streaming;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote Interface for Java RMI File Transfer Service.
 * Defines remote methods for room management and file streaming.
 */
public interface FileTransferService extends Remote {

    // Room Management Remote Methods
    String createRoom() throws RemoteException;
    void createRoom(String roomCode) throws RemoteException;

    boolean joinRoom(String roomCode, String username) throws RemoteException;
    boolean joinRoom(String roomCode) throws RemoteException;

    // File Streaming Remote Methods
    void sendFile(String roomCode, String fileName, byte[] fileData) throws RemoteException;
    byte[] receiveFile(String roomCode) throws RemoteException;
    String getLatestFileName(String roomCode) throws RemoteException;

    // Direct Upload / Download Aliases
    void uploadFile(String roomCode, byte[] fileData, String fileName) throws RemoteException;
    byte[] downloadFile(String roomCode) throws RemoteException;
}
