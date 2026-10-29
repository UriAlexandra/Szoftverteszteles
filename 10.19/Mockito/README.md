# BankAccount értesítéssel – Mockito tesztek

Szoftvertesztelés – 4. feladat. A 3. feladat `BankAccount` osztálya kiegészítve egy értesítési szolgáltatással. A külső függőségeket (értesítés, árfolyam) a tesztek Mockito-mockokkal helyettesítik.

## Projekt felépítése

```
10.29-Mockito/
├── pom.xml                         Maven (Java 17, JUnit 5.11, Mockito 5.14.2)
├── src/main/java/org/example/
│   ├── BankAccount.java            a 3. feladat bankszámlája
│   ├── NotificationService.java    értesítési szolgáltatás (interfész)
│   ├── BankService.java            befizetés + értesítés
│   ├── ExchangeRateService.java    árfolyam-szolgáltatás (interfész)
│   └── CurrencyConverter.java      devizaátváltás az árfolyam-szolgáltatással
└── src/test/java/org/example/
    ├── BankServiceTest.java        mock(), verify()
    ├── ExchangeRateServiceTest.java when().thenReturn()
    └── BankAccountTest.java        a 3. feladat tesztjei
```

## Az osztályok

**`NotificationService`:** interfész egyetlen metódussal, `void send(String message)`. Éles környezetben egy valódi e-mail- vagy SMS-szolgáltatás valósítaná meg. A projektben nincs ilyen megvalósítás, a tesztek mockot használnak helyette.

**`BankService`:** az értesítési szolgáltatást konstruktoron keresztül kapja (Dependency Injection).

```java
NotificationService notificationService = mock(NotificationService.class);
BankService bankService = new BankService(notificationService);
```

A `deposit(BankAccount account, double amount)` befizet a számlára, majd értesítést küld, például: `Sikeres befizetés: 1000.00 Ft. Új egyenleg: 1000.00 Ft.` Ha a befizetés sikertelen (0, negatív, `NaN` vagy végtelen összeg), a számla `IllegalArgumentException` kivételt dob, és értesítés nem megy ki.

**`ExchangeRateService`:** interfész a `double getRate(String from, String to)` metódussal, amely megadja, hogy 1 egység `from` deviza hány egység `to` devizát ér.

**`CurrencyConverter`:** az árfolyam-szolgáltatást szintén konstruktoron kapja. A `convert(amount, from, to)` metódus az árfolyammal szorozva vált át. Negatív összegnél `IllegalArgumentException`, érvénytelen (0 vagy negatív) árfolyamnál `IllegalStateException` kivételt dob.

## Futtatás és tesztelés

**Szükséges:** JDK 17 vagy újabb, valamint Maven 3.9+. Ehelyett az IntelliJ IDEA beépített Mavenje is megfelel.

A `10.29-Mockito` mappában:

```bash
mvn clean test
```

A Maven letölti a JUnit 5-öt és a Mockito-t, lefordítja a kódot, és lefuttatja mindhárom tesztosztályt. A részletes eredmény a `target/surefire-reports` mappába kerül.

**IntelliJ IDEA-ban:** **File → Open**, majd a `10.29-Mockito` mappa `pom.xml` fájlja, és megnyitás projektként. Utána jobb klikk a `src/test/java` mappán, majd **Run 'All Tests'**.

JDK 21 vagy újabb esetén a Mockito figyelmeztetést írhat ki arról, hogy futás közben tölti be a saját Java-agentjét („Mockito is currently self-attaching…”). Ez nem hiba, a tesztek ettől függetlenül lefutnak.

## A használt Mockito-eszközök

| Eszköz | Mire való | Hol |
|---|---|---|
| `mock(...)` | Mock objektum létrehozása valódi szolgáltatás helyett | mindkét Mockito-tesztosztály `setUp()` metódusa |
| `verify(...)` | Megtörtént-e a hívás, és a megfelelő paraméterrel | `BankServiceTest`, `ExchangeRateServiceTest` |
| `verify(..., times(n))`, `verify(..., never())` | A hívások száma, illetve hogy egyáltalán nem történt hívás | sikeres és sikertelen befizetés |
| `verifyNoInteractions(...)`, `verifyNoMoreInteractions(...)` | Semmilyen, illetve semmi további hívás nem történt a mockon | sikertelen befizetés, pontos üzenet |
| `when(...).thenReturn(...)` | A mock válaszának előre beállítása | `ExchangeRateServiceTest` |
| `anyString()` | Bármely szöveges paraméter elfogadása a `verify`-ban | a `send()` hívásának ellenőrzése |
| `mockingDetails(...).isMock()` | Valóban mockot kapott-e a `BankService` | 5. kötelező eset |

## Tesztek

### `BankServiceTest` – mockolt értesítési szolgáltatással

| # | Feladatbeli eset | Ellenőrzés |
|---|---|---|
| 1 | A befizetés növeli az egyenleget | `deposit(account, 1000)` után az egyenleg 1000 |
| 2 | Sikeres befizetés után meghívódik a `send()` | `verify(notificationService, times(1)).send(anyString())` |
| 3 | A megfelelő értesítés megy ki | `verify(notificationService).send("Sikeres befizetés: 1000.00 Ft. Új egyenleg: 1000.00 Ft.")` és `verifyNoMoreInteractions` |
| 4 | Sikertelen befizetésnél nincs értesítés | 0, -100 és `NaN` összegre: kivétel, `verify(..., never())`, `verifyNoInteractions`, az egyenleg 0 marad |
| 5 | Nem valódi értesítési szolgáltatás | `mockingDetails(notificationService).isMock()` |

**További esetek:**
- **Több befizetés:** mindegyikről külön értesítés megy ki, az aktuális egyenleggel.
- **Sikeres és sikertelen befizetés együtt:** csak a sikeresről megy értesítés.
- **Tört összeg:** két tizedesjeggyel jelenik meg az üzenetben.
- **`null` számla:** `NullPointerException`, és nincs értesítés.
- **`null` szolgáltatás:** nem adható át a konstruktornak.

### `ExchangeRateServiceTest` – beállított viselkedésű mock

**Árfolyamok beállítása `when().thenReturn()`-nel:**
- **Egy beállított árfolyam:** a mock a beállított EUR → HUF 400.0-t adja vissza, és a `verify` ellenőrzi a hívást.
- **Több devizapár:** mindegyikre a hozzá beállított árfolyam jön (EUR → HUF 400.0, USD → HUF 360.0).
- **Be nem állított hívás:** a mock az alapértéket, 0.0-t adja.
- **Egymás utáni értékek:** a `thenReturn(400.0, 405.5, 410.0)` hívásonként a következő értéket adja, a sor végén az utolsó ismétlődik.

**A beállított árfolyam használata (`CurrencyConverter`):**
- **Átváltás:** 10 EUR = 4000 HUF a 400.0-s árfolyammal.
- **Változó árfolyam:** az átváltás követi az árfolyam változását.
- **Érvénytelen árfolyam:** 0 vagy negatív árfolyamnál `IllegalStateException`.
- **Negatív összeg:** hiba, és az árfolyam-szolgáltatás meg sem hívódik (`verifyNoInteractions`).

### `BankAccountTest`

A 3. feladat 33 tesztesete változatlanul.
