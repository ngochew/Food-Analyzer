import exceptions.FoodAnalyzerClientExcepiton;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FoodAnalyzerServer {

    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 20000;
    private static final int API_KEY_LENGTH = 40;

    private int serverPort;
    private boolean serverState;
    private Map<SocketAddress, String> userApiKeys = new LinkedHashMap<>();

    File serverCache;

    public FoodAnalyzerServer(int serverPort) {
        this.serverPort = serverPort;
        serverState = false;
        serverCache = new File("Food-Analyzer-Cache");
        try {
            serverCache.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create cache file.", e);
        }
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            this.serverState = true;
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, this.serverPort));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

            while (serverState) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext() && serverState) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = socketChannel.read(buffer);
                        if (r <= 0) {
                            System.out.println("nothing to read, will close channel" + System.lineSeparator());
                            socketChannel.close();
                            break;
                        }

                        buffer.flip();
                        byte[] byteArray = new byte[buffer.remaining()];
                        buffer.get(byteArray);
                        String message = new String(byteArray, "UTF-8");
                        message = message.replace(System.lineSeparator(), "");

                        System.out.println("Received request from " + socketChannel.getRemoteAddress() +
                                System.lineSeparator() + ">" + message + System.lineSeparator());

                        String response;
                        final String replyHeader = "Replied to " + socketChannel.getRemoteAddress()
                                + System.lineSeparator();

                        // Check if message is an API-Key and save it
                        if (!message.trim().contains(" ") && message.length() == API_KEY_LENGTH) {
                            this.userApiKeys.put(socketChannel.getRemoteAddress(), message);
                            response = "API-key received." + System.lineSeparator();
                            socketChannel.write(ByteBuffer.wrap(response.getBytes()));
                            System.out.println(replyHeader + response);

                        } else { //Handle request from Client if Apikey is provided.
                            if (this.userApiKeys.containsKey(socketChannel.getRemoteAddress())) {
                                RequestHandler.handleRequest(message, socketChannel,
                                        this.userApiKeys.get(socketChannel.getRemoteAddress()));
                            } else { // Request API-Key if not provided.
                                response = "Please provide a valid API-key." + System.lineSeparator();
                                socketChannel.write(ByteBuffer.wrap(response.getBytes()));
                                System.out.println(replyHeader + response);
                            }
                        }

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException | FoodAnalyzerClientExcepiton e) {
            throw new RuntimeException("There was a problem with Food-Analyzer-Server", e);
        }
    }

    public static void main(String[] args) {
        FoodAnalyzerServer server = new FoodAnalyzerServer(7777);
        server.start();
    }
}

