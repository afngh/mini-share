package streaming;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * FileUtils - Standalone file conversion utility for Java RMI File Sharing.
 * 
 * =========================================================================================
 * WHY THIS CONVERSION IS NECESSARY FOR RMI:
 * =========================================================================================
 * In Java RMI (Remote Method Invocation), parameters passed to remote methods must either be:
 *   1. Remote objects (implementing java.rmi.Remote), or
 *   2. Serializable objects (implementing java.io.Serializable).
 * 
 * Standard stream objects like FileInputStream or FileOutputStream manage native OS file
 * handles tied to a specific host's filesystem and are NOT Serializable. Therefore, RMI
 * cannot directly stream FileInputStream instances across network remote calls.
 * 
 * SOLUTION:
 * To transfer files via RMI, the sender reads the file contents into a primitive byte array
 * (byte[]), which is natively Serializable. This byte array is transmitted over the wire in
 * the remote method call (e.g., uploadFile(roomCode, byte[] fileData, fileName)), and the
 * receiver uses bytesToFile() to write the byte array back to disk.
 * =========================================================================================
 */
public class FileUtils {

    /**
     * Reads the full contents of a file into a byte array.
     *
     * @param file The target file to read.
     * @return A byte array containing the entire content of the file.
     * @throws IOException If the file does not exist, is invalid, or cannot be read.
     */
    public static byte[] fileToBytes(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File reference cannot be null.");
        }
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found or is not a regular file: " + file.getAbsolutePath());
        }
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Writes a byte array out to a file at the specified output path.
     * Creates the file if it does not exist, and overwrites it if it already exists.
     *
     * @param data       The byte array to write to disk.
     * @param outputPath The target file destination path.
     * @throws IOException If an I/O error occurs while writing the file or creating parent directories.
     */
    public static void bytesToFile(byte[] data, String outputPath) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("Byte data cannot be null.");
        }
        if (outputPath == null || outputPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Output path cannot be null or empty.");
        }

        Path path = Paths.get(outputPath);
        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        Files.write(path, data, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.TRUNCATE_EXISTING, 
            StandardOpenOption.WRITE);
    }

    /**
     * Self-contained test suite to verify byte-for-byte fidelity of file conversions.
     */
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Running FileUtils Verification Test...");
        System.out.println("==================================================");

        String testInputPath = "test_sample_input.txt";
        String testOutputPath = "test_sample_output.txt";

        File inputFile = new File(testInputPath);
        File outputFile = new File(testOutputPath);

        try {
            // 1. Prepare sample test data
            String testContent = "Hello RMI Mini-Share! Testing byte streaming conversion.\n" +
                                 "Line 2: 1234567890 !@#$%^&*()_+\n" +
                                 "Line 3: Binary data check -> \u0000\u0001\u0002\u00FF";
            byte[] expectedBytes = testContent.getBytes("UTF-8");
            
            // Create test input file
            bytesToFile(expectedBytes, testInputPath);
            System.out.println("[+] Created sample input file: " + inputFile.getAbsolutePath() + " (" + inputFile.length() + " bytes)");

            // 2. Convert file to byte[]
            byte[] readBytes = fileToBytes(inputFile);
            System.out.println("[+] Successfully converted file to byte[] (" + readBytes.length + " bytes)");

            // 3. Write byte[] back out to new file path
            bytesToFile(readBytes, testOutputPath);
            System.out.println("[+] Successfully wrote byte[] back to file: " + outputFile.getAbsolutePath() + " (" + outputFile.length() + " bytes)");

            // 4. Verify byte-for-byte equality and file attributes
            byte[] outputBytes = fileToBytes(outputFile);

            boolean sizeMatch = (inputFile.length() == outputFile.length());
            boolean contentMatch = Arrays.equals(expectedBytes, outputBytes);

            System.out.println("--------------------------------------------------");
            System.out.println(" Verification Results:");
            System.out.println("  - Input file size:  " + inputFile.length() + " bytes");
            System.out.println("  - Output file size: " + outputFile.length() + " bytes");
            System.out.println("  - File size check:  " + (sizeMatch ? "MATCH" : "MISMATCH"));
            System.out.println("  - Byte equality:    " + (contentMatch ? "MATCH" : "MISMATCH"));
            System.out.println("--------------------------------------------------");

            if (sizeMatch && contentMatch) {
                System.out.println(">> TEST RESULT: PASS [Byte-for-byte identical verification successful]");
            } else {
                System.err.println(">> TEST RESULT: FAIL [Data corruption or mismatch detected]");
            }

        } catch (Exception e) {
            System.err.println(">> TEST RESULT: FAIL with exception:");
            e.printStackTrace();
        } finally {
            // Cleanup temporary test files
            if (inputFile.exists()) inputFile.delete();
            if (outputFile.exists()) outputFile.delete();
            System.out.println("[+] Cleaned up temporary test files.");
            System.out.println("==================================================");
        }
    }
}
