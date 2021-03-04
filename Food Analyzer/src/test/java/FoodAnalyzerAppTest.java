import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FoodAnalyzerAppTest {
    @BeforeClass
    public static void startServer() {
        Thread serverThread = new Thread(() -> {
            FoodAnalyzerServer foodAnalyzerServer = new FoodAnalyzerServer(7777);
            foodAnalyzerServer.start();
        });
        serverThread.start();
    }

    @Test
    public void testGetFoodRequest() {

        final int SERVER_PORT = 7777;
        final String SERVER_HOST = "localhost";
        ByteBuffer buffer = ByteBuffer.allocateDirect(20000);

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            String message = "CerWOT7RllqUgvZBTZ8tReezkoitn9AsPKmbilkz";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill

            message = "get-food raffaello treat";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill
            buffer.flip(); // switch to reading mode

            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);
            String reply = new String(byteArray, "UTF-8"); // buffer drain

            assertTrue(reply.startsWith("Name: raffaello treat"));
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    @Test
    public void testFoodRequestWithoutProvidingApiKey() {

        final int SERVER_PORT = 7777;
        final String SERVER_HOST = "localhost";
        ByteBuffer buffer = ByteBuffer.allocateDirect(20000);

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            String message = "get-food raffaello treat";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill
            buffer.flip(); // switch to reading mode

            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);
            String reply = new String(byteArray, "UTF-8"); // buffer drain

            assertEquals("Please provide a valid API-key." + System.lineSeparator(), reply);
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    @Test
    public void testGetFoodWithFdcIdRequest() {

        final int SERVER_PORT = 7777;
        final String SERVER_HOST = "localhost";
        ByteBuffer buffer = ByteBuffer.allocateDirect(20000);

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            String message = "CerWOT7RllqUgvZBTZ8tReezkoitn9AsPKmbilkz";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill

            message = "get-food 1223633";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill
            buffer.flip(); // switch to reading mode

            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);
            String reply = new String(byteArray, "UTF-8"); // buffer drain

            assertTrue(reply.startsWith("Name: lindt-gold-bunny"));
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    @Test
    public void testGetFoodByBarcodeRequest() {

        final int SERVER_PORT = 7777;
        final String SERVER_HOST = "localhost";
        ByteBuffer buffer = ByteBuffer.allocateDirect(20000);

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            String message = "CerWOT7RllqUgvZBTZ8tReezkoitn9AsPKmbilkz";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill

            message = "get-food 1223648";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill

            message = "get-food-by-barcode 009800146130";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill
            buffer.flip(); // switch to reading mode

            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);
            String reply = new String(byteArray, "UTF-8"); // buffer drain

            assertTrue(reply.startsWith("Name: raffaello treat"));
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    @Test
    public void testUnknownRequest() {

        final int SERVER_PORT = 7777;
        final String SERVER_HOST = "localhost";
        ByteBuffer buffer = ByteBuffer.allocateDirect(20000);

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            String message = "CerWOT7RllqUgvZBTZ8tReezkoitn9AsPKmbilkz";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill

            message = "get random food";

            buffer.clear(); // switch to writing mode
            buffer.put(message.getBytes()); // buffer fill

            buffer.flip(); // switch to reading mode
            socketChannel.write(buffer); // buffer drain

            buffer.clear(); // switch to writing mode
            socketChannel.read(buffer); // buffer fill
            buffer.flip(); // switch to reading mode

            byte[] byteArray = new byte[buffer.remaining()];
            buffer.get(byteArray);
            String reply = new String(byteArray, "UTF-8"); // buffer drain

            assertEquals("Unknown request - please use " + System.lineSeparator() +
                    "get-food {food name} / get-food-report {FoodData Central ID / get-food-by-barcode {barcode} / " +
                    "get-food-by-barcode --img {Path of barcode image}" + System.lineSeparator(), reply);
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
