import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class TriangleTest {

    @Test
    @DisplayName("Kivétel dobása érvénytelen háromszög esetén")
    void testInvalidTriangleThrowsException() {
        assertThrows(ArithmeticException.class, () -> new Triangle(1, 2, 10));
    }

    @Test
    @DisplayName("Kerület és terület tesztelése (Implementált függvények)")
    void testPerimeterAndArea() {
        Triangle t = new Triangle(3, 4, 5);
        assertEquals(12, t.getPerimeter(), "A kerületnek 12-nek kell lennie");
        assertEquals(6.0, t.getArea(), 0.001, "A területnek 6-nak kell lennie");
    }

    /* 
     * 2. pont: Ha a metódusok még nem lennének implementálva, így tesztelnénk a kivételt:
     * @Test
     * void testUnimplementedMethodsThrowException() {
     *     Triangle t = new Triangle(3, 4, 5);
     *     assertThrows(UnsupportedOperationException.class, () -> t.isRightAngled());
     * }
     */

    // 3. pont: Adatvezérelt teszt Stream segítségével
    @ParameterizedTest(name = "{0}: a={1}, b={2}, c={3}")
    @MethodSource("testData")
    @DisplayName("Háromszög típusok tesztelése paraméterezve")
    void testTriangleTypes(String name, int a, int b, int c, boolean expectedIsosceles, boolean expectedEquilateral, boolean expectedRightAngled) {
        Triangle t = new Triangle(a, b, c);
        
        assertEquals(expectedIsosceles, t.isIsosceles(), "Egyenlő szárú hiba: " + name);
        assertEquals(expectedEquilateral, t.isEquilateral(), "Egyenlő oldalú hiba: " + name);
        assertEquals(expectedRightAngled, t.isRightAngled(), "Derékszögű hiba: " + name);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
            // Név, a, b, c, isIsosceles, isEquilateral, isRightAngled
            Arguments.of("Általános", 5, 6, 7, false, false, false),
            Arguments.of("Egyenlő szárú", 5, 5, 8, true, false, false),
            // (A feladat diáján elírás van: a 10,8,8 egyenlő szárú, nem egyenlő oldalú. Itt a helyes egyenlő oldalú adat szerepel)
            Arguments.of("Egyenlő oldalú", 6, 6, 6, true, true, false), 
            Arguments.of("Derékszögű (Pitagoraszi-számhármas)", 3, 4, 5, false, false, true)
        );
    }
}