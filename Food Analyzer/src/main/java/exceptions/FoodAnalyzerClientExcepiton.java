package exceptions;

public class FoodAnalyzerClientExcepiton extends Exception {
    public FoodAnalyzerClientExcepiton(String message) {
        super(message);
    }

    public FoodAnalyzerClientExcepiton(String message, Exception e) {
        super(message, e);
    }
}
