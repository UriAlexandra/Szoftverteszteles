import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorTest {

    private Calculator calculator;

    // 5. @BeforeAll: Csak egyszer fut le a tesztek legelején
    @BeforeAll
    static void beforeAll() {
        System.out.println("START - Tesztek futtatásának kezdete");
    }

    // 5. @AfterAll: Csak egyszer fut le az összes teszt végén
    @AfterAll
    static void afterAll() {
        System.out.println("END - Tesztek futtatásának vége");
    }

    // 4. @BeforeEach: Minden egyes @Test előtt lefut, új példányt hoz létre
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("2 + 3 eredménye 5")
    void additionWorks() {
        // Arrange
        int a = 2;
        int b = 3;

        // Act
        int result = calculator.add(a, b);

        // Assert (az elvárt érték az első, a kapott a második)
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Kivonás működik: 10 - 3 = 7")
    void subtractionWorks() {
        assertEquals(7, calculator.subtract(10, 3));
    }

    @Test
    @DisplayName("Szorzás működik: 4 * 5 = 20")
    void multiplicationWorks() {
        assertEquals(20, calculator.multiply(4, 5));
    }

    @Test
    @DisplayName("Osztás működik: 20 / 4 = 5")
    void divisionWorks() {
        assertEquals(5, calculator.divide(20, 4));
    }

    // 2. Különféle assert függvények bemutatása
    @Test
    @DisplayName("Különféle assertionök (assertTrue, assertFalse, stb.) bemutatása")
    void simpleAssertions() {
        assertEquals(4, 2 + 2);
        assertNotEquals(5, 2 + 2);
        assertTrue(10 > 5);
        assertFalse(3 > 10);

        String name = "Oliver";
        assertNotNull(name);
    }

    // 6. Kivételek tesztelése (Exception ellenőrzése)
    @Test
    @DisplayName("Nullával osztás kivételt dob")
    void divisionByZeroThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.divide(10, 0)
        );
        assertEquals("Nullával nem lehet osztani!", exception.getMessage());    }

    // 7. assertAll: Több dolog egyidejű ellenőrzése
    @Test
    @DisplayName("Több feltétel egyidejű ellenőrzése")
    void severalAssertions() {
        int result = 10;

        assertAll(
                () -> assertTrue(result > 0),
                () -> assertTrue(result < 100),
                () -> assertEquals(10, result)
        );
    }

    // 9. @Disabled: Ideiglenesen kikapcsolt teszt
    //@Test
    //@Disabled("A funkció még nincs implementálva (pl. hatványozás)")
    //void unfinishedTest() {}

    // 9. Hatványozás tesztelése (a korábbi @Disabled helyett)
    @Test
    @DisplayName("Hatványozás működik: 2^3 = 8, 5^0 = 1")
    void powerWorks() {
        // Alapeset: 2 a köbön az 8
        assertEquals(8.0, calculator.power(2, 3));

        // Bármely nullától különböző szám a nulladikon 1
        assertEquals(1.0, calculator.power(5, 0));

        // Negatív kitevő: 2^(-1) = 0.5
        assertEquals(0.5, calculator.power(2, -1));
    }

    // 10. Paraméterezett tesztek: Ugyanaz a logika több bemeneti adattal
    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "3, 4, 7",
            "-1, 1, 0",
            "10, 20, 30"
    })
    @DisplayName("Összeadás paraméterezve több adatsorral")
    void parameterizedAdditionWorks(int a, int b, int expected) {
        assertEquals(expected, calculator.add(a, b));
    }

    // 11. @ValueSource: Egyszerű listás paraméterezés
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 10, 100})
    @DisplayName("Pozitív számok tesztelése ValueSource használatával")
    void positiveNumbersArePositive(int number) {
        assertTrue(number > 0);
    }
}
