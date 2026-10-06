import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    @DisplayName("1. feladat: Default User objektum alapállapota")
    void testDefaultUserInitialState() {
        User user = new User();

        // A user objektum nem null
        assertNotNull(user, "A user objektum nem lehet null");

        // A username és password mezők értéke null
        assertNull(user.getUserName(), "A userName mezőnek null-nak kell lennie");
        assertNull(user.getPassword(), "A password mezőnek null-nak kell lennie");

        // Az id és a loginCount mezők értéke 0
        assertEquals(0, user.getId(), "Az id mezőnek 0-nak kell lennie");
        assertEquals(0, user.getLoginCount(), "A loginCount mezőnek 0-nak kell lennie");

        // Az updatePwd metódus hívása hibát dobott
        assertThrows(UnsupportedOperationException.class, 
            () -> user.updatePwd("newPassword", true), 
            "Az updatePwd metódusnak UnsupportedOperationException-t kell dobnia");
    }

    @Test
    @DisplayName("2. feladat: isLoggedIn alapállapotban false")
    void testIsLoggedInInitiallyFalse() {
        User user = new User();
        // Írjunk egy isLoggedIn unit tesztet úgy, hogy false értéket várjunk el
        assertFalse(user.isLoggedIn(), "Kezdetben a isLoggedIn értékének false-nak kell lennie");
    }

    @Test
    @DisplayName("2. feladat: login() után isLoggedIn true, loginCount nő")
    void testLoginChangesStateAndCounter() {
        User user = new User();
        
        // Hozzunk létre egy üres login metódust (ezt a User.java-ban képzeljük el, 
        // majd implementáljuk). Teszteljük, hogy a login() meghívása után:
        user.login();

        // 1. isLoggedIn == true
        assertTrue(user.isLoggedIn(), "Sikeres bejelentkezés után isLoggedIn-nek true-nak kell lennie");
        
        // 2. loginCount == 1 (vagy egyszerűen megnőtt eggyel)
        assertEquals(1, user.getLoginCount(), "Egy bejelentkezés után a loginCount-nak 1-nek kell lennie");
    }

    @Test
    @DisplayName("2. feladat: logout() működése")
    void testLogout() {
        User user = new User();
        
        // Először bejelentkezünk, hogy utána kijelentkezhessünk
        user.login();
        assertTrue(user.isLoggedIn(), "Előfeltétel: sikeres bejelentkezés");

        // Kijelentkezés
        user.logout();

        // Hasonló módon implementáljuk a logout metódust is: 
        // ellenőrizzük, hogy false lesz az isLoggedIn
        assertFalse(user.isLoggedIn(), "Kijelentkezés után isLoggedIn-nek false-nak kell lennie");
        
        // A loginCount nem változhat a kijelentkezés hatására
        assertEquals(1, user.getLoginCount(), "A kijelentkezés nem csökkentheti a loginCount értékét");
    }
    
    @Test
    @DisplayName("Többszöri login tesztelése (opcionális extra ellenőrzés)")
    void testMultipleLogins() {
         User user = new User();
         user.login();
         user.logout();
         user.login();
         
         assertEquals(2, user.getLoginCount(), "Két bejelentkezés után a loginCount értéke 2 kell legyen");
         assertTrue(user.isLoggedIn());
    }
}