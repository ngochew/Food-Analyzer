package dto;

public class Nutrient {
    private String nutrientName;
    private String unitName;
    private String derivationDescription;
    private double value;

    public Nutrient(String nutrientName, String unitName, String derivationDescription, double value) {
        this.nutrientName = nutrientName;
        this.unitName = unitName;
        this.derivationDescription = derivationDescription;
        this.value = value;
    }


    public String getNutrientName() {
        return nutrientName;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getDerivationDescription() {
        return derivationDescription;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "  nutrientName: " + nutrientName + " | " +
                "   unitName: " + unitName + " | " +
                "   derivationDescription: " + derivationDescription + " | " +
                "   value: " + value + System.lineSeparator();
    }
}
