import java.util.HashSet;
import java.util.Set;

public class ServerState {
    private final Set<String> activeClients = new HashSet<>();

    public synchronized void addClient(String clientIp) {
        activeClients.add(clientIp);
    }

    public synchronized void removeClient(String clientIp) {
        activeClients.remove(clientIp);
    }

    public synchronized int getActiveClientCount() {
        return activeClients.size();
    }
}