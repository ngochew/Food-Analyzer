package dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Food {

    @SerializedName("foodSearchCriteria")
    private FoodSearchCriteria name;

    @SerializedName("foods")
    private FoodInformation[] foodInformation;

    public Food(FoodSearchCriteria name, FoodInformation[] foodInformation) {
        this.name = name;
        this.foodInformation = foodInformation;
    }

    public FoodSearchCriteria getName() {
        return name;
    }

    public FoodInformation[] getFoodInformation() {
        return foodInformation;
    }

    @Override
    public String toString() {
        return "Food name: " + name + Arrays.toString(foodInformation);
    }
}
