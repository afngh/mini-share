package streaming;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Utility for file-to-byte-array streaming helper (Send & Receive)
 */
public class FileStreamer {

    /**
     * Reads a local File into a byte array for streaming via RMI.
     */
    public static byte[] fileToByteArray(File file) throws IOException {
        return FileUtils.fileToBytes(file);
    }

    /**
     * Writes received RMI byte array data to disk at destinationPath.
     */
    public static void byteArrayToFile(byte[] data, String destinationPath) throws IOException {
        FileUtils.bytesToFile(data, destinationPath);
    }

    /**
     * High-level upload helper: converts file to byte[] and sends via RMI FileTransferService.
     */
    public static void uploadFile(FileTransferService service, String roomCode, File file) throws IOException, RemoteException {
        byte[] data = fileToByteArray(file);
        service.sendFile(roomCode, file.getName(), data);
    }

    /**
     * High-level download helper: receives byte[] via RMI FileTransferService and writes to destinationPath.
     */
    public static void downloadFile(FileTransferService service, String roomCode, String destinationPath) throws IOException, RemoteException {
        byte[] data = service.receiveFile(roomCode);
        byteArrayToFile(data, destinationPath);
    }
    // Methods for converting File to byte[] array and byte[] array to File 
}
