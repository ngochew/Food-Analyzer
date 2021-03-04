import com.google.zxing.LuminanceSource;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Reader;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.FormatException;
import com.google.zxing.ChecksumException;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class FoodAnalyzerClient {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 20000;
    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.print("Please provide an API-key in order to proceed." + System.lineSeparator());

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine(); // read a line from the console

                //Image to barcode conversion
                if (message.contains("get-food-by-barcode --img")) {
                    InputStream barCodeInputStream =
                            new FileInputStream(message.substring("get-food-by-barcode --img".length()).trim());
                    BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);
                    LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    Reader reader = new MultiFormatReader();
                    Result barcode = reader.decode(bitmap);
                    message = "get-food-by-barcode " + barcode;
                }

                //System.out.println("Sending message <" + message + "> to the server...");
                buffer.clear(); // switch to writing mode
                buffer.put((message + System.lineSeparator()).getBytes()); // buffer fill

                buffer.flip(); // switch to reading mode
                socketChannel.write(buffer); // buffer drain

                if ("disconnect".equals(message)) {
                    break;
                }

                buffer.clear(); // switch to writing mode
                socketChannel.read(buffer); // buffer fill
                buffer.flip(); // switch to reading mode

                byte[] byteArray = new byte[buffer.remaining()];
                buffer.get(byteArray);
                String reply = new String(byteArray, "UTF-8"); // buffer drain

                System.out.println(reply);
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        } catch (FormatException | ChecksumException | NotFoundException e) {
            throw new RuntimeException("There was a problem with Food-Analyzer client", e);
        }
    }
}