package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            // Spin up standard RMI Registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            
            FileTransferServiceImpl service = new FileTransferServiceImpl();
            registry.rebind("FileTransferService", service);
            
            System.out.println("========================================");
            System.out.println("🚀 Java RMI Server Architecture is Live!");
            System.out.println("Listening on local port 1099...");
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("Fatal Server Crash: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
