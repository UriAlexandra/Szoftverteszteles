public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    // Az osztásnál érdemes double-t visszaadni és lekezelni a 0-val való osztást
    public double divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Nullával nem lehet osztani!");
        }
        return (double) a / b;
    }

    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }
}
