public class Main {
    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 9000;
        int maxClients = 4;

        ServerState state = new ServerState();

        TcpServer server = new TcpServer(serverIp, serverPort, maxClients, state);
        SimpleHttpServer httpServer = new SimpleHttpServer(8080, state);

        System.out.println("Server configuration is ready.");
        System.out.println("IP: " + server.getServerIp());
        System.out.println("Port: " + server.getServerPort());
        System.out.println("Max clients: " + server.getMaxClients());

        new Thread(() -> server.start()).start();
        httpServer.start();
    }
}