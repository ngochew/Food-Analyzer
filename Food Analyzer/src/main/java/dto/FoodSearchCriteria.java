package dto;

import com.google.gson.annotations.SerializedName;

public class FoodSearchCriteria {

    @SerializedName("query")
    private String name;

    public FoodSearchCriteria(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
