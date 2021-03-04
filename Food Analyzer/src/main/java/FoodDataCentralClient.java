import com.google.gson.Gson;
import dto.Food;
import exceptions.FoodAnalyzerClientExcepiton;
import exceptions.FoodNotFoundException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FoodDataCentralClient {
    private HttpClient foodDataCentralClient;

    public FoodDataCentralClient(HttpClient FoodDataCentralClient) {
        this.foodDataCentralClient = FoodDataCentralClient;
    }

    // Sends request to FDC Rest-API

    public Food requestFoodByName(String foodName, String apiKey) throws FoodAnalyzerClientExcepiton {
        HttpResponse<String> response = null;
        try {
            URI uri = new URI("https", "api.nal.usda.gov", "/fdc/v1/foods/search",
                    String.format("query=%s&requireAllWords=true&api_key=%s", foodName, apiKey), null);

            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            response = foodDataCentralClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        assert response != null;
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), Food.class);

        } else if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new FoodNotFoundException("Couldn't find food related to that search criteria.");
        }

        throw new FoodAnalyzerClientExcepiton("There was a problem with Food-Analyzer-Client");
    }

    public Food requestFoodById(String foodDataCentralId, String apiKey) throws FoodAnalyzerClientExcepiton {
        HttpResponse<String> response = null;
        try {
            URI uri = new URI("https", "api.nal.usda.gov", "/fdc/v1/food/" + foodDataCentralId,
                    String.format("api_key=%s", apiKey), null);

            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            response = foodDataCentralClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new FoodAnalyzerClientExcepiton("There was a problem with Food-Analyzer-Client", e);
        }

        assert response != null;
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();

            return gson.fromJson(response.body(), Food.class);

        } else if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new FoodNotFoundException("Couldn't find food related to that search criteria.");
        }

        throw new FoodAnalyzerClientExcepiton("There was a problem with Food-Analyzer-Client");
    }
}
