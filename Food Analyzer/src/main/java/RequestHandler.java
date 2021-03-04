import com.google.gson.Gson;
import dto.Food;
import exceptions.FoodAnalyzerClientExcepiton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;


public class RequestHandler {

    private static final Path cacheFile = Path.of("Food-Analyzer-Cache");
    private static final FoodDataCentralClient foodDataCentralClient =
            new FoodDataCentralClient(HttpClient.newBuilder().build());

    public static void handleRequest(String message, SocketChannel socketChannel, String apiKey)
            throws FoodAnalyzerClientExcepiton {

        if (message == null) {
            throw new IllegalArgumentException();
        }

        try {
            if (message.equals("disconnect")) {
                socketChannel.close();
                return;
            }

            String[] splittedMessage = message.split(" ");
            final String replyHeader = "Replied to " + socketChannel.getRemoteAddress() + System.lineSeparator();

            switch (splittedMessage[0]) {
                case "get-food" -> {
                    String foodName = message.substring("get-food".length()).strip();
                    Food food = getFoodByName(foodName, apiKey);
                    String response = getHumanReadableOutput(food) + System.lineSeparator();
                    socketChannel.write(ByteBuffer.wrap(response.getBytes()));
                    System.out.println(replyHeader + response);
                }
                case "get-food-report" -> {
                    String foodDataCentralId = message.substring("get-food-report".length()).strip();
                    Food food = getFoodById(foodDataCentralId, apiKey);
                    String response;
                    if (getHumanReadableOutput(food).length() != 0) {
                        response = getHumanReadableOutput(food) + System.lineSeparator();

                    } else {
                        response = "There isn't information about such ID, " +
                                "please try again." + System.lineSeparator();
                    }
                    socketChannel.write(ByteBuffer.wrap(response.getBytes()));
                    System.out.println(replyHeader + response);
                }
                case "get-food-by-barcode" -> {
                    String barcode = message.substring("get-food-by-barcode".length()).strip();
                    Food food = getFoodByBarcode(barcode);
                    String response;
                    if (food != null) {
                        response = getHumanReadableOutput(food) + System.lineSeparator();

                    } else {
                        response = "There isn't information about such barcode, " +
                                "please try searching by name or fdcid." + System.lineSeparator();
                    }
                    socketChannel.write(ByteBuffer.wrap(response.getBytes()));
                    System.out.println(replyHeader + response);
                }
                default -> {
                    String response = "Unknown request - please use " + System.lineSeparator() +
                            "get-food {food name} / get-food-report {FoodData Central ID " +
                            "/ get-food-by-barcode {barcode} / get-food-by-barcode --img {Path of barcode image}"
                            + System.lineSeparator();
                    socketChannel.write(ByteBuffer.wrap(response.getBytes()));
                    System.out.println(replyHeader + response);
                }
            }
        } catch (IOException e) {
            throw new FoodAnalyzerClientExcepiton("There was a problem with Food-Analyzer-Client", e);
        }
    }

    private static Food getFoodByName(String foodName, String apiKey) throws FoodAnalyzerClientExcepiton {
        if (foodName == null) {
            throw new IllegalArgumentException();
        }

        try (FileWriter fileWriter = new FileWriter(String.valueOf(cacheFile), true);
             BufferedReader reader = Files.newBufferedReader(cacheFile);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            Gson gson = new Gson();

            // Check cache if information already exists.
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(foodName)) {
                    return gson.fromJson(line, Food.class);
                }
            }

            Food food = foodDataCentralClient.requestFoodByName(foodName, apiKey);
            String foodInformation = gson.toJson(food);
            writer.write(System.lineSeparator() + foodInformation);
            return food;
        } catch (IOException e) {
            throw new FoodAnalyzerClientExcepiton("There was a problem with Food-Analyzer-Client", e);
        }
    }

    private static Food getFoodById(String foodDataCentralId, String apiKey) throws FoodAnalyzerClientExcepiton {
        if (foodDataCentralId == null) {
            throw new IllegalArgumentException();
        }

        try (FileWriter fileWriter = new FileWriter(String.valueOf(cacheFile), true);
             BufferedReader reader = Files.newBufferedReader(cacheFile);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            Gson gson = new Gson();

            // Check cache if information already exists.
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(foodDataCentralId)) {
                    return gson.fromJson(line, Food.class);
                }
            }

            Food food = foodDataCentralClient.requestFoodByName(foodDataCentralId, apiKey);
            String foodInformation = gson.toJson(food);
            writer.write(System.lineSeparator() + foodInformation);
            return food;
        } catch (IOException e) {
            throw new FoodAnalyzerClientExcepiton("There was a problem with Food-Analyzer-Client", e);
        }
    }

    private static Food getFoodByBarcode(String barcode) throws FoodAnalyzerClientExcepiton {
        if (barcode == null) {
            throw new IllegalArgumentException();
        }

        try (var reader = Files.newBufferedReader(cacheFile)) {

            Gson gson = new Gson();

            // Check cache if information already exists.
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(barcode)) {
                    return gson.fromJson(line, Food.class);
                }
            }

        } catch (IOException e) {
            throw new FoodAnalyzerClientExcepiton("There was a problem with Food-Analyzer-Client", e);
        }

        return null;
    }

    private static String getHumanReadableOutput(Food food) {
        if (food == null) {
            return "No information about such food.";
        }
        final int PRODUCTS_LIMIT_COUNT = 5;
        StringBuilder humanReadableOutput = new StringBuilder();
        for (int i = 0; i < Math.min(PRODUCTS_LIMIT_COUNT, food.getFoodInformation().length); i++) {
            humanReadableOutput.append("Name: ")
                    .append(food.getName().getName())
                    .append(System.lineSeparator());
            humanReadableOutput.append("FoodData Central ID: ")
                    .append(food.getFoodInformation()[i].toString());
        }
        return humanReadableOutput.toString();
    }
}
