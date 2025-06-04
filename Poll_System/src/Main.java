import bg.sofia.uni.fmi.mjt.poll.server.PollServer;
import bg.sofia.uni.fmi.mjt.poll.server.repository.InMemoryPollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.utils.TextTokenizer;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        PollRepository repostiory = new InMemoryPollRepository();
        PollServer server = new PollServer(1066, repostiory);
        server.start();
        server.end();
    }
}