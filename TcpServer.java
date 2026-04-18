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
}