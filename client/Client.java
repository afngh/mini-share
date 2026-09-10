package client;

import streaming.FileTransferService;
import java.rmi.Naming;

// Client entry point
public class Client {
    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        if (args.length > 0) {
            serverIp = args[0];
        }

        System.out.println("Connecting to FileService at " + serverIp + "...");
        try {
            // RMI Lookup
            FileTransferService service = (FileTransferService) Naming.lookup("rmi://" + serverIp + "/FileService");
            System.out.println("Successfully connected to the server!\n");
            
            // Pass the remote service to the CLI
            ClientCLI cli = new ClientCLI(service);
            cli.start();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
