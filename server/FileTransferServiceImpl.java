package server;

import streaming.FileTransferService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;
import java.util.Set;

public class FileTransferServiceImpl extends UnicastRemoteObject implements FileTransferService {
    
    // Tracks active rooms
    private final Set<String> activeRooms = new HashSet<>();
    // Storage for the file data per room: RoomCode -> File Bytes
    private final ConcurrentHashMap<String, byte[]> roomFiles = new ConcurrentHashMap<>();
    // Storage for the file name per room: RoomCode -> File Name
    private final ConcurrentHashMap<String, String> roomFileNames = new ConcurrentHashMap<>();

    public FileTransferServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void createRoom(String roomCode) throws RemoteException {
        activeRooms.add(roomCode);
        System.out.println("RMI Server: Room [" + roomCode + "] successfully created by client.");
    }

    @Override
    public synchronized boolean joinRoom(String roomCode) throws RemoteException {
        if (activeRooms.contains(roomCode)) {
            System.out.println("RMI Server: Client joined room: " + roomCode);
            return true;
        }
        System.out.println("RMI Server: Join failed. Room " + roomCode + " does not exist.");
        return false;
    }

    @Override
    public void uploadFile(String roomCode, byte[] fileData, String fileName) throws RemoteException {
        if (!activeRooms.contains(roomCode)) throw new RemoteException("Target room does not exist.");
        
        roomFiles.put(roomCode, fileData);
        roomFileNames.put(roomCode, fileName);
        System.out.println("RMI Server: Stored uploaded file [" + fileName + "] inside room: " + roomCode);
    }

    @Override
    public byte[] downloadFile(String roomCode) throws RemoteException {
        if (!roomFiles.containsKey(roomCode)) {
            throw new RemoteException("No file available in room: " + roomCode);
        }
        System.out.println("RMI Server: Transmitting file [" + roomFileNames.get(roomCode) + "] out of room: " + roomCode);
        return roomFiles.get(roomCode);
    }
}
