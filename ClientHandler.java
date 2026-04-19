import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Arrays;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ServerState state;
    private static final String SERVER_DIR = "./server_data/";

    public ClientHandler(Socket socket, ServerState state) {
        this.socket = socket;
        this.state = state;
        new File(SERVER_DIR).mkdirs();
    }

   public void run() {
        String clientIp = socket.getInetAddress().getHostAddress();
        String clientId = clientIp + ":" + socket.getPort();
        String role = "read"; 

        state.addClient(clientId);

        try (
            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // FIX 1: Moved this inside the try block so the Exception is caught
            socket.setSoTimeout(300000);

            out.println("You are connected to the server");

            String message;
            while ((message = in.readLine()) != null) {

                if (message.startsWith("ROLE:")) {
                    role = message.substring(5).toLowerCase().trim();
                    System.out.println(clientId + " identified as: " + role);
                    out.println("Role accepted: " + role);
                    
             
                    if (role.equals("admin")) {
                        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                    } else {
                        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                    }
                    continue;
                }

                if (!role.equals("admin")) {
                    try {
                        Thread.sleep(300); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Message from " + clientId + " [" + role + "]: " + message);
                state.addMessage(clientId + ": " + message);

                if (isAdminCommand(message)) {
                    if (!role.equals("admin")) {
                        out.println("[DENIED] You do not have permission to run: " + message);
                        continue;
                    }
                    handleAdminCommand(message, out);
                    continue;
                }

                if (message.startsWith("/read ")) {
                    handleRead(message, out);
                    continue;
                }

                if (message.startsWith("/download ")) {
                    handleDownload(message, out);
                    continue;
                }

                if (message.startsWith("/info ")) {
                    handleInfo(message, out);
                    continue;
                }

                switch (message.toUpperCase().trim()) {
                    case "PING"  -> out.println("PONG");
                    case "HELLO" -> out.println("Hello client");
                    case "BYE"  -> { out.println("Goodbye"); return; }
                    default     -> out.println("Server received: " + message);
                }
            }

        } catch (SocketTimeoutException e) {
            System.out.println("Client timeout: " + clientId);
        } catch (Exception e) {
            System.out.println("Error with client " + clientId + ": " + e);
        } finally {
            state.removeClient(clientId);
            System.out.println("Client disconnected: " + clientId);
        }
    }


    private boolean isAdminCommand(String msg) {
               return msg.equals("/list")   ||
               msg.startsWith("/upload ") ||
               msg.startsWith("/delete ") ||
               msg.startsWith("/search ");
    }

    private void handleAdminCommand(String message, PrintWriter out) {
        if (message.equals("/list")) {
            File dir = new File(SERVER_DIR);
            String[] files = dir.list();
            if (files != null && files.length > 0) {
                out.println("[/list] Files on server: " + Arrays.toString(files));
            } else {
                out.println("[/list] Server directory is empty.");
            }

        } else if (message.startsWith("/upload ")) {
            String[] parts = message.substring(8).trim().split(" ", 2);
            if (parts.length < 2) {
                out.println("[/upload] Usage: /upload <filename> <content>");
                return;
            }
            String filename = parts[0];
            String content = parts[1];
            
            try (FileWriter fw = new FileWriter(SERVER_DIR + filename)) {
                fw.write(content);
                out.println("[/upload] File '" + filename + "' created/updated successfully.");
            } catch (IOException e) {
                out.println("[/upload] Error writing file: " + e.getMessage());
            }

        } else if (message.startsWith("/delete ")) {
            String filename = message.substring(8).trim();
            File f = new File(SERVER_DIR + filename);
            if (f.delete()) {
                out.println("[/delete] File '" + filename + "' has been deleted.");
            } else {
                out.println("[/delete] Failed to delete '" + filename + "'. File might not exist.");
            }

        } else if (message.startsWith("/search ")) {
            String keyword = message.substring(8).trim();
            File dir = new File(SERVER_DIR);
            File[] files = dir.listFiles((d, name) -> name.contains(keyword));
            
            if (files != null && files.length > 0) {
                String[] foundNames = Arrays.stream(files).map(File::getName).toArray(String[]::new);
                out.println("[/search] Found files: " + Arrays.toString(foundNames));
            } else {
                out.println("[/search] No files found matching '" + keyword + "'.");
            }
        }
    }

    private void handleRead(String message, PrintWriter out) {
        String filename = message.substring(6).trim();
        try {
            String content = Files.readString(Path.of(SERVER_DIR + filename));
            out.println("[/read] Content of '" + filename + "':\n" + content);
        } catch (IOException e) {
            out.println("[/read] Error reading file: File may not exist.");
        }
    }

    private void handleDownload(String message, PrintWriter out) {
        String filename = message.substring(10).trim();
        try {
            String content = Files.readString(Path.of(SERVER_DIR + filename));
            out.println("[/download] --- START OF FILE " + filename + " ---");
            out.println(content);
            out.println("[/download] --- END OF FILE ---");
        } catch (IOException e) {
            out.println("[/download] Error: File not found or unreadable.");
        }
    }

    private void handleInfo(String message, PrintWriter out) {
        String filename = message.substring(6).trim();
        File f = new File(SERVER_DIR + filename);
        if (f.exists()) {
            out.println("[/info] File: " + filename + " | Size: " + f.length() + " bytes | Last Modified: " + f.lastModified());
        } else {
            out.println("[/info] File '" + filename + "' does not exist.");
        }
    }
}