package client;

import streaming.FileTransferService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientCLI {
    private FileTransferService service;
    private Scanner scanner;
    private String currentRoom = null;

    public ClientCLI(FileTransferService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            if (currentRoom == null) {
                showScreen1();
            } else {
                showScreen2();
            }
        }
    }

    private void showScreen1() {
        System.out.println("=== Mini-Share ===");
        System.out.println("1. Create Room");
        System.out.println("2. Join Room");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine();
        try {
            if (choice.equals("1")) {
                System.out.print("Enter a new room code: ");
                String roomCode = scanner.nextLine();
                service.createRoom(roomCode);
                System.out.println("Room " + roomCode + " created successfully.");
                currentRoom = roomCode;
            } else if (choice.equals("2")) {
                System.out.print("Enter room code to join: ");
                String roomCode = scanner.nextLine();
                service.joinRoom(roomCode);
                System.out.println("Joined room " + roomCode + " successfully.");
                currentRoom = roomCode;
            } else if (choice.equals("3")) {
                System.out.println("Exiting...");
                System.exit(0);
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println();
    }

    private void showScreen2() {
        System.out.println("=== Room: " + currentRoom + " ===");
        System.out.println("1. Send File");
        System.out.println("2. Receive File");
        System.out.println("3. Leave Room");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();
        try {
            if (choice.equals("1")) {
                sendFile();
            } else if (choice.equals("2")) {
                receiveFile();
            } else if (choice.equals("3")) {
                currentRoom = null;
                System.out.println("Left the room.\n");
            } else {
                System.out.println("Invalid option. Please try again.\n");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void sendFile() {
        System.out.print("Enter absolute path of the file to send: ");
        String filePath = scanner.nextLine();
        File file = new File(filePath);

        if (!file.exists() || file.isDirectory()) {
            System.out.println("Invalid file path.\n");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileData = new byte[(int) file.length()];
            int bytesRead = fis.read(fileData);
            
            if (bytesRead != fileData.length) {
                System.out.println("Warning: Could not read the entire file.");
            }

            System.out.println("Uploading file...");
            service.uploadFile(currentRoom, fileData, file.getName());
            System.out.println("File uploaded successfully!\n");
        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to upload over RMI: " + e.getMessage());
        }
    }

    private void receiveFile() {
        System.out.println("Downloading file from room " + currentRoom + "...");
        try {
            byte[] fileData = service.downloadFile(currentRoom);
            if (fileData == null || fileData.length == 0) {
                System.out.println("No file found in this room.\n");
                return;
            }

            System.out.print("Enter local path to save the downloaded file (e.g., /tmp/downloaded.txt): ");
            String savePath = scanner.nextLine();
            File file = new File(savePath);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileData);
                System.out.println("File saved successfully to " + savePath + "!\n");
            } catch (IOException e) {
                System.err.println("Failed to write file to disk: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Failed to download over RMI: " + e.getMessage());
        }
    }
}
