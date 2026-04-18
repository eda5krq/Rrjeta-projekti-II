import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ServerState state;

    public ClientHandler(Socket socket, ServerState state) {
        this.socket = socket;
        this.state = state;
    }

    public void run() {
        String clientIp = socket.getInetAddress().getHostAddress();
        String clientId = clientIp + ":" + socket.getPort();
        String role = "read"; 

        state.addClient(clientId);

        try (
            socket;
            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            socket.setSoTimeout(30000);
            out.println("You are connected to the server");

            String message;
            while ((message = in.readLine()) != null) {

                if (message.startsWith("ROLE:")) {
                    role = message.substring(5).toLowerCase();
                    System.out.println(clientId + " identified as: " + role);
                    out.println("Role accepted: " + role);
                    continue;
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
        return msg.startsWith("/list")   ||
               msg.startsWith("/upload ") ||
               msg.startsWith("/delete ") ||
               msg.startsWith("/search ");
    }

    private void handleAdminCommand(String message, PrintWriter out) {
        if (message.equals("/list")) {
            out.println("[/list] File listing not yet implemented.");

        } else if (message.startsWith("/upload ")) {
            String filename = message.substring(8).trim();
            out.println("[/upload] Upload for '" + filename + "' not yet implemented.");

        } else if (message.startsWith("/delete ")) {
            String filename = message.substring(8).trim();
            out.println("[/delete] Delete for '" + filename + "' not yet implemented.");

        } else if (message.startsWith("/search ")) {
            String keyword = message.substring(8).trim();
            out.println("[/search] Search for '" + keyword + "' not yet implemented.");
        }
    }

    private void handleRead(String message, PrintWriter out) {
        String filename = message.substring(6).trim();
        out.println("[/read] Read for '" + filename + "' not yet implemented.");
    }

    private void handleDownload(String message, PrintWriter out) {
        String filename = message.substring(10).trim();
        out.println("[/download] Download for '" + filename + "' not yet implemented.");
    }

    private void handleInfo(String message, PrintWriter out) {
        String filename = message.substring(6).trim();
        out.println("[/info] Info for '" + filename + "' not yet implemented.");
    }
}