import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    @Test
    void addTwoNumbers() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }

    @Test
    void subtractTwoNumbers() {
        Calculator calculator = new Calculator();
        int result = calculator.subtract(10, 4);
        assertEquals(6, result); // Elvárt eredmény: 6
    }

    @Test
    void multiplyTwoNumbers() {
        Calculator calculator = new Calculator();
        int result = calculator.multiply(5, 4);
        assertEquals(20, result); // Elvárt eredmény: 20
    }

    @Test
    void divideTwoNumbers() {
        Calculator calculator = new Calculator();
        double result = calculator.divide(15, 3);
        assertEquals(5.0, result); // Elvárt eredmény: 5.0
    }

    @Test
    void divideByZeroThrowsException() {
        Calculator calculator = new Calculator();
        // Azt teszteljük, hogy kivételt dob-e a program, ha a második paraméter 0
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10, 0);
        });
    }

    @Test
    void addNegativeNumbers() {
        Calculator calculator = new Calculator();
        int result = calculator.add(-2, -3);
        assertEquals(-5, result); // Negatív számok tesztelése
    }
}