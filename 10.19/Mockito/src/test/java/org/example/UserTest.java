package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private User mockedUser;

    @Mock
    private UserDatabase mockedDatabase;

    @Test
    void testUserMockDataAndVerification() {
        // 1. Mockolt objektum beállítása a kért adatokkal
        when(mockedUser.getUserName()).thenReturn("TesztElek");
        when(mockedUser.getPassword()).thenReturn("TOPSECRET");
        when(mockedUser.isLoggedIn()).thenReturn(true);
        when(mockedUser.getId()).thenReturn(123);

        // Adatok lekérdezése (hogy a verify() később sikeres legyen)
        assertEquals("TesztElek", mockedUser.getUserName());
        assertEquals("TOPSECRET", mockedUser.getPassword());
        assertTrue(mockedUser.isLoggedIn());
        assertEquals(123, mockedUser.getId());

        // 2. A verify() függvény használatával ellenőrizzük az egyes metódusok meghívását
        verify(mockedUser).getUserName();
        verify(mockedUser).getPassword();
        verify(mockedUser).isLoggedIn();
        verify(mockedUser).getId();
    }

    @Test
    void testUserUpdatePwdCallsDatabase() {
        // Valódi User példányosítása mockolt adatbázissal
        User realUser = new User(123, "TesztElek", "TOPSECRET", true, mockedDatabase);

        // Mivel a UserDatabase setPassword metódusa a valóságban kivételt dobna,
        // mockolhatjuk ezt a viselkedést a tesztben is:
        doThrow(new UnsupportedOperationException()).when(mockedDatabase).setPassword(123, "NEW_PWD");

        // Ellenőrizzük, hogy a User.updatePwd meghívása kiváltja-e a kivételt
        assertThrows(UnsupportedOperationException.class, () -> {
            realUser.updatePwd("NEW_PWD");
        });

        // Végül ellenőrizzük, hogy a User valóban meghívta-e az adatbázis setPassword függvényét a megfelelő paraméterekkel
        verify(mockedDatabase).setPassword(123, "NEW_PWD");
    }
}