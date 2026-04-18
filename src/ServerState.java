import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ServerState {
    private Set<String> activeClients = new HashSet<>();
    private ArrayList<String> messages = new ArrayList<>();

    public synchronized void addClient(String clientIp) {
        activeClients.add(clientIp);
    }

    public synchronized void removeClient(String clientIp) {
        activeClients.remove(clientIp);
    }

    public synchronized int getActiveClientCount() {
        return activeClients.size();
    }

    public synchronized void addMessage(String message) {
        messages.add(message);
    }

    public synchronized ArrayList<String> getMessages() {
        return messages;
    }

    public synchronized int getMessageCount() {
        return messages.size();
    }

    public synchronized Set<String> getActiveClients() {
        return activeClients;
    }
}