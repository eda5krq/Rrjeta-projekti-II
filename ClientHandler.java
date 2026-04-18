import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ServerState state;

    public ClientHandler(Socket socket, ServerState state) {
        this.socket = socket;
        this.state = state;
    }

    @Override
    public void run() {
        String clientIp = socket.getInetAddress().getHostAddress();
        state.addClient(clientIp);

        System.out.println("Client handler started for: " + clientIp);

        try {
            Thread.sleep(10000);
            socket.close();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            state.removeClient(clientIp);
            System.out.println("Client disconnected: " + clientIp);
        }
    }
}