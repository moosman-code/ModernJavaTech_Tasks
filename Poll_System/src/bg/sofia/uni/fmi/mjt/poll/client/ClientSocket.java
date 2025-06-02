package bg.sofia.uni.fmi.mjt.poll.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientSocket {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1066;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try (SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            Scanner scanner = new Scanner(System.in)) {

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            System.out.print("Enter message: ");
            String clientInput = scanner.nextLine();
            while (!clientInput.equalsIgnoreCase("disconnect")) {

                byte[] bytes = clientInput.getBytes(StandardCharsets.UTF_8);
                buffer.clear();
                buffer.put(bytes);
                buffer.flip();
                clientChannel.write(buffer);

                buffer.clear();
                int readBytes = clientChannel.read(buffer);
                if (readBytes == -1) {
                    System.out.println("Server closed the connection");
                    break;
                }
                buffer.flip(); // ADD THIS

                bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String response = new String(bytes, StandardCharsets.UTF_8);
                System.out.println("Server response: " + response); // FIX OUTPUT HERE

                System.out.print("Enter message: ");
                clientInput = scanner.nextLine();

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
