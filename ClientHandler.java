import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ServerState state;

    public ClientHandler(Socket socket, ServerState state) {
        this.socket = socket;
        this.state = state;
    }

    public void run() {
        String clientIp = socket.getInetAddress().getHostAddress();
        state.addClient(clientIp);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("You are connected to the server");

            String message;

            while ((message = in.readLine()) != null) {
                System.out.println("Message from " + clientIp + ": " + message);

                if (message.equals("PING")) {
                    out.println("PONG");
                } else if (message.equals("HELLO")) {
                    out.println("Hello client");
                } else if (message.equals("BYE")) {
                    out.println("Goodbye");
                    break;
                } else {
                    out.println("Server received: " + message);
                }
            }

            socket.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        state.removeClient(clientIp);
        System.out.println("Client disconnected: " + clientIp);
    }
}