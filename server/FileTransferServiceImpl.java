package server;

import streaming.FileTransferService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileTransferServiceImpl extends UnicastRemoteObject implements FileTransferService {
    
    // Memory state storage for rooms and files
    private final ConcurrentHashMap<String, List<String>> rooms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, byte[]> roomFiles = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> roomFileNames = new ConcurrentHashMap<>();

    public FileTransferServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized String createRoom() throws RemoteException {
        String roomCode = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        rooms.put(roomCode, new ArrayList<>());
        System.out.println("RMI Server: Room [" + roomCode + "] successfully created.");
        return roomCode;
    }

    @Override
    public synchronized boolean joinRoom(String roomCode, String username) throws RemoteException {
        if (rooms.containsKey(roomCode)) {
            rooms.get(roomCode).add(username);
            System.out.println("RMI Server: User [" + username + "] joined room: " + roomCode);
            return true;
        }
        System.out.println("RMI Server: Join attempt failed. Room " + roomCode + " not found.");
        return false;
    }

    @Override
    public void sendFile(String roomCode, String fileName, byte[] fileData) throws RemoteException {
        if (!rooms.containsKey(roomCode)) throw new RemoteException("Target room does not exist.");
        
        roomFiles.put(roomCode, fileData);
        roomFileNames.put(roomCode, fileName);
        System.out.println("RMI Server: Stored file [" + fileName + "] inside room: " + roomCode);
    }

    @Override
    public byte[] receiveFile(String roomCode) throws RemoteException {
        if (!roomFiles.containsKey(roomCode)) {
            throw new RemoteException("No file available in room: " + roomCode);
        }
        return roomFiles.get(roomCode);
    }

    @Override
    public String getLatestFileName(String roomCode) throws RemoteException {
        return roomFileNames.getOrDefault(roomCode, "unknown_file");
    }
}
