import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    private final String serverIp;
    private final int serverPort;
    private final int maxClients;

    public TcpServer(String serverIp, int serverPort, int maxClients) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.maxClients = maxClients;
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

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error while starting server: " + e.getMessage());
        }
    }
}