package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.utils.JacksonParser;
import bg.sofia.uni.fmi.mjt.poll.server.utils.TextTokenizer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PollServer {
    private static final int BUFFER_SIZE = 1024;

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buffer;

    public PollRepository pollRepository;

    public PollServer(int port, PollRepository pollRepository) {
        this.pollRepository = pollRepository;

        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("localhost", port));
            serverChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            this.serverChannel = serverChannel;
            this.selector = selector;
            this.buffer = buffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {
            System.out.println("Server is listening...");

            while (true) {
                int activeChannels = selector.select();
                if (activeChannels == 0) {
                    continue;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        buffer.clear();
                        int readBytes = clientChannel.read(buffer);
                        if (readBytes < 0) {
                            System.out.println("Client has closed the connection");
                            clientChannel.close();
                            continue;
                        }

                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String input = new String(bytes, StandardCharsets.UTF_8);

                        String response = handleRequest(input);

                        bytes = response.getBytes(StandardCharsets.UTF_8);
                        buffer.clear();
                        buffer.put(bytes);

                        buffer.flip();
                        clientChannel.write(buffer);
                    }
                    else if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel newClientChannel = serverSocketChannel.accept();
                        newClientChannel.configureBlocking(false);
                        newClientChannel.register(selector, SelectionKey.OP_READ);
                    }

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String handleRequest(String input) {
        String[] arguments = input.replaceAll("\\s+", " ").split(" ", 2);
        String jsonResponse = null;

        switch(arguments[0].toLowerCase()) {
            case "create-poll" -> {
                Poll newPoll = Poll.of(arguments[1]);
                int pollId = pollRepository.addPoll(newPoll);
                jsonResponse = JacksonParser.parseToJsonMessage(
                        "OK", "Poll " + pollId + " created successfully");
            }
            case "list-polls" -> {
                Map<Integer, Poll> polls = pollRepository.getAllPolls();
                jsonResponse = polls.isEmpty()
                        ? JacksonParser.parseToJsonMessage("ERROR", "No active polls available.")
                        : JacksonParser.parseToJSONObject("OK", "polls", polls);
            }
            case "submit-vote" -> {
                String[] details = arguments[1].split(" ");
                if (details.length != 2) {
                    throw new IllegalArgumentException("Invalid request format");
                }

                try {
                    int pollId = Integer.parseInt(details[0]);
                    Poll poll = pollRepository.getPoll(pollId);

                    if (poll == null) {
                        jsonResponse = JacksonParser.parseToJsonMessage(
                                "ERROR", "Poll with ID" + pollId + "does not exist.");
                    }
                    else if (!poll.options().containsKey(details[1])) {
                        jsonResponse = JacksonParser.parseToJsonMessage(
                                "ERROR", "Invalid option. Option" + details[1] + "does not exist.");
                    }
                    else {
                        int increasedVote = poll.options().get(details[1]) + 1;
                        poll.options().put(details[1], increasedVote);
                        jsonResponse = JacksonParser.parseToJsonMessage(
                                "OK", "Vote submitted successfully for option: " + details[1]);
                    }
                }
                catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
            case "default" -> {
                jsonResponse = JacksonParser.parseToJsonMessage(
                        "ERROR", "Invalid client command");
            }
        }

            return jsonResponse;
    }

    public void end() {
        try {
            serverChannel.close();
            selector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
