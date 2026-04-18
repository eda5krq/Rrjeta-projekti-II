import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer {

    private int port;
    private ServerState state;

    public SimpleHttpServer(int port, ServerState state) {
        this.port = port;
        this.state = state;
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext("/stats", exchange -> {
                String response =
                        "{\n" +
                        "\"activeClients\": " + state.getActiveClientCount() + ",\n" +
                        "\"clientIps\": \"" + state.getActiveClients() + "\",\n" +
                        "\"messageCount\": " + state.getMessageCount() + ",\n" +
                        "\"messages\": \"" + state.getMessages() + "\"\n" +
                        "}";

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });

            server.start();
            System.out.println("HTTP server is running on port " + port);

        } catch (Exception e) {
            System.out.println("HTTP server error: " + e.getMessage());
        }
    }
}