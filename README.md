# cn-project
# mini-S

🧑‍💻 Vikram: RMI Foundation & Room Management (The Server)

Hour 1: Define the Remote Interface (e.g., FileTransferService). Create the Server application that starts the LocateRegistry and binds the service.
Hour 2: Implement the Room Management logic inside the server. Use a HashMap<String, List<String>> to track which user/IP is in which room. Add remote methods like createRoom(String roomCode) and joinRoom(String roomCode).
Hour 3: Assist Developer B with byte array streaming. Help debug connection issues when connecting the second device via IP address.

🧑‍💻 Afnan: File Streaming Logic (Send & Receive)

Hour 1: Write the core file-to-byte-array conversion logic. Because RMI cannot easily stream standard FileInputStream objects directly, you must read the file into a byte[] array to send it over the network.
Hour 2: Add the uploadFile(String roomCode, byte[] fileData, String fileName) and downloadFile(String roomCode) methods to the Remote Interface and implement them.
Hour 3: Connect the file upload/download hooks to the UI/Console application that Developer C is building.

🧑‍💻 Harshith: Client CLI / Simple UI & State Machine

Hour 1: Build the Client starter boilerplate. Write the RMI lookup code (Naming.lookup("rmi://<SERVER_IP>/FileService")) and ensure it successfully pings the server.
Hour 2: Build a simple Command Line Interface (CLI) loop.
       - Screen 1: Prompt user to 1. Create Room or 2. Join Room.
       - Screen 2: Once in a room, prompt user to 1. Send File or 2. Receive File.
Hour 3: Integrate Developer B's file-sharing methods into the CLI options. Run end-to-end network tests between the two actual devices.
