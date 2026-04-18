public class Main {
    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 9000;
        int maxClients = 4;

        TcpServer server = new TcpServer(serverIp, serverPort, maxClients);

        System.out.println("Server configuration is ready.");
        System.out.println("IP: " + server.getServerIp());
        System.out.println("Port: " + server.getServerPort());
        System.out.println("Max clients: " + server.getMaxClients());
    }
}