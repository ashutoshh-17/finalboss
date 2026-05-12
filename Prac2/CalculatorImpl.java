import CalculatorApp.*;

public class CalculatorImpl extends CalculatorPOA {

    public float add(float a, float b) {
        return a + b;
    }

    public float sub(float a, float b) {
        return a - b;
    }

    public float mul(float a, float b) {
        return a * b;
    }

    public float div(float a, float b) {
        return a / b;
    }
}