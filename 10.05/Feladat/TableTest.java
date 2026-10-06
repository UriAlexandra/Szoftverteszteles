import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableTest {

    @Test
    @DisplayName("Magasság állítása sikeres, ha állítható és megfelelő az intervallum")
    void testSetHeightValid() {
        Table table = new Table(120, 80, 70, 70, "Barna", 4);
        table.setHeight(100);
        assertEquals(100, table.getCurrentHeight());
    }

    @Test
    @DisplayName("Magasság állítása hibát dob, ha érvénytelen az intervallum")
    void testSetHeightInvalidThrowsException() {
        Table table = new Table(120, 80, 70, 70, "Barna", 4);
        assertThrows(IllegalArgumentException.class, () -> table.setHeight(250));
        assertThrows(IllegalArgumentException.class, () -> table.setHeight(-10));
    }

    @Test
    @DisplayName("Magasság állítása hibát dob, ha nem állítható")
    void testSetHeightNotAdjustableThrowsException() {
        Table table = new Table(120, 80, 70, "Fehér", 4);
        assertThrows(UnsupportedOperationException.class, () -> table.setHeight(80));
    }

    @Test
    @DisplayName("Terület kiszámítása helyes")
    void testArea() {
        Table table = new Table(100, 200, 75, "Fekete", 4);
        assertEquals(20000, table.area());
    }

    @Test
    @DisplayName("Férőhely (Kapacitás) számítása helyes (60cm/fő)")
    void testGetCapacity() {
        // 130/60 = 2 fő egy oldalon -> 4 fő a hosszán
        // 80/60 = 1 fő egy oldalon -> 2 fő a széltén
        // Összesen: 6 fő
        Table table = new Table(80, 130, 75, "Piros", 4);
        assertEquals(6, table.getCapacity());
    }

    @Test
    @DisplayName("Repaint sikeresen módosítja a színt")
    void testRepaint() {
        Table table = new Table(100, 100, 70, "Fehér", 4);
        table.repaint("Kék");
        assertEquals("Kék", table.getColor());
    }

    @Test
    @DisplayName("isStable igaz, ha a lábak száma >= 3")
    void testIsStable() {
        Table stableTable = new Table(100, 100, 70, "Fehér", 3);
        assertTrue(stableTable.isStable());

        Table unstableTable = new Table(100, 100, 70, "Fehér", 2);
        assertFalse(unstableTable.isStable());
    }

    @Test
    @DisplayName("isFoldable igaz, ha állítható és lábak száma >= 4")
    void testIsFoldable() {
        Table foldableTable = new Table(100, 100, 70, 75, "Fehér", 4); // Állítható, 4 láb
        assertTrue(foldableTable.isFoldable());

        Table notFoldableLegs = new Table(100, 100, 70, 75, "Fehér", 3); // Állítható, de csak 3 láb
        assertFalse(notFoldableLegs.isFoldable());

        Table notFoldableAdjust = new Table(100, 100, 70, "Fehér", 4); // 4 láb, de NEM állítható
        assertFalse(notFoldableAdjust.isFoldable());
    }

    @Test
    @DisplayName("Asztal kerületének (Perimeter) kiszámítása")
    void testGetPerimeter() {
        Table table = new Table(150, 80, 70, "Zöld", 4);
        // 2 * (150 + 80) = 460
        assertEquals(460, table.getPerimeter());
    }
}