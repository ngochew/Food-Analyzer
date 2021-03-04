package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class FoodInformation {

    @SerializedName("fdcId")
    private int foodDataCentralId;
    private String description;
    private String dataType;

    @SerializedName("gtinUpc")
    private String barcode;
    private String ingredients;
    private Nutrient[] foodNutrients;

    public FoodInformation(int foodDataCentralId, String description,
                           String dataType, String barcode, String ingredients, Nutrient[] foodNutrients) {
        this.foodDataCentralId = foodDataCentralId;
        this.description = description;
        this.dataType = dataType;
        this.barcode = barcode;
        this.ingredients = ingredients;
        this.foodNutrients = foodNutrients;
    }

    public int getFoodDataCentralId() {
        return foodDataCentralId;
    }

    public String getDescription() {
        return description;
    }

    public String getDataType() {
        return dataType;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getIngredients() {
        return ingredients;
    }

    public Nutrient[] getFoodNutrients() {
        return foodNutrients;
    }

    @Override
    public String toString() {
        return "FoodInformation: " + System.lineSeparator() +
                "   foodDataCentralId: " + foodDataCentralId + System.lineSeparator() +
                "   description: " + description + System.lineSeparator() +
                "   dataType: " + dataType + System.lineSeparator() +
                "   barcode: " + barcode + System.lineSeparator() +
                "   ingredients: " + ingredients + System.lineSeparator() +
                "   foodNutrients: " + Arrays.toString(foodNutrients) + System.lineSeparator();
    }
}
