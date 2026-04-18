import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    private final String serverIp;
    private final int serverPort;
    private final int maxClients;
    private final ServerState state;

    public TcpServer(String serverIp, int serverPort, int maxClients, ServerState state) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.maxClients = maxClients;
        this.state = state;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Server is listening on port " + serverPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientIp = clientSocket.getInetAddress().getHostAddress();

                if (state.getActiveClientCount() >= maxClients) {
                    System.out.println("Connection refused for: " + clientIp);
                    clientSocket.close();
                    continue;
                }

                System.out.println("Client connected: " + clientIp);
                ClientHandler handler = new ClientHandler(clientSocket, state);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("Error while starting server: " + e.getMessage());
        }
    }
}