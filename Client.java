import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static final String SERVER_IP = "10.1.2.204";
    private static final int SERVER_PORT = 9000;

    private String role; 
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String role) {
        this.role = role;
    }

    public void start() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to server at " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Role: " + role);

            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            
            out.println("ROLE:" + role);


            String welcome = in.readLine();
            System.out.println("Server: " + welcome);

            
            Thread readerThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println("Server: " + response);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Type your commands below:");

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;

                if (isAdminCommand(input)) {
                    if (role.equals("admin")) {
                        out.println(input);
                    } else {
                        System.out.println("[DENIED] You only have read permission. Admin commands are not allowed.");
                    }
                } else {
                    out.println(input);
                }

                if (input.equals("BYE")) break;
            }

            socket.close();

        } catch (ConnectException e) {
            System.out.println("Could not connect to server at " + SERVER_IP + ":" + SERVER_PORT);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean isAdminCommand(String input) {
        String[] adminCommands = {"/list", "/upload", "/delete", "/search"};
        for (String cmd : adminCommands) {
            if (input.startsWith(cmd)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String role = (args.length > 0) ? args[0].toLowerCase() : "read";

        if (!role.equals("admin") && !role.equals("read")) {
            System.out.println("Invalid role. Use 'admin' or 'read'.");
            return;
        }

        new Client(role).start();
    }
}