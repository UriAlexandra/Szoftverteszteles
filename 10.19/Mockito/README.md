# BankAccount értesítéssel – Mockito tesztek

Szoftvertesztelés – 4. feladat. A 3. feladat `BankAccount` osztálya kiegészítve egy értesítési szolgáltatással. A külső függőségeket (értesítés, árfolyam) a tesztek Mockito-mockokkal helyettesítik.

## Projekt felépítése

```
10.19/Mockito/
├── pom.xml                         Maven (Java 17, JUnit 5.11, Mockito 5.14.2)
├── src/main/java/org/example/
│   ├── BankAccount.java            a 3. feladat bankszámlája
│   ├── NotificationService.java    értesítési szolgáltatás (interfész)
│   ├── BankService.java            befizetés + értesítés
│   ├── ExchangeRateService.java    árfolyam-szolgáltatás (interfész)
│   ├── CurrencyConverter.java      devizaátváltás az árfolyam-szolgáltatással
│   ├── User.java                   órai gyakorló feladat
│   ├── UserDatabase.java           órai gyakorló feladat
│   └── App.java                    a Maven-sablon kiinduló osztálya
└── src/test/java/org/example/
    ├── BankServiceTest.java        mock(), verify()
    ├── ExchangeRateServiceTest.java when().thenReturn()
    ├── BankAccountTest.java        a 3. feladat tesztjei
    ├── UserTest.java               órai gyakorló feladat (@Mock, doThrow)
    └── AppTest.java                a Maven-sablon tesztje
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

## A követelmények teljesítése

| Követelmény | Megvalósítás | Teszt |
|---|---|---|
| `NotificationService` interfész | `void send(String message)` | – |
| `BankService`: befizetés, majd értesítés | `BankService.deposit(account, amount)` | `BankServiceTest` 1–3. eset |
| Dependency Injection konstruktoron keresztül | `new BankService(notificationService)` | minden `BankServiceTest`; `null` szolgáltatás nem adható át |
| 1. A befizetés növeli az egyenleget | | `BankServiceTest` 1. eset |
| 2. Sikeres befizetés után meghívódik a `send()` | | `BankServiceTest` 2. eset |
| 3. `verify()`: a megfelelő értesítés megy ki | | `BankServiceTest` 3. eset |
| 4. Sikertelen befizetésnél nincs értesítés | | `BankServiceTest` 4. eset (0, -100, `NaN`) |
| 5. Nincs valódi külső értesítési szolgáltatás | a projektben nincs megvalósító osztály, a tesztek `mock()`-ot kapnak | `BankServiceTest` 5. eset |
| `ExchangeRateService` interfész | `double getRate(String from, String to)` | – |
| Árfolyam beállítása `when().thenReturn()`-nel | | `ExchangeRateServiceTest` |
| Teszt a beállított válasz ellenőrzésére | | „A mock a beállított EUR → HUF árfolyamot (400.0) adja vissza” |
| Futtatás `mvn clean test`-tel | | lásd [Futtatás és tesztelés](#futtatás-és-tesztelés) |
| `mock()`, `verify()`, `when().thenReturn()` használata | | lásd [A használt Mockito-eszközök](#a-használt-mockito-eszközök) |

## Futtatás és tesztelés

**Szükséges:** JDK 17 vagy újabb, valamint Maven 3.9+. Ehelyett az IntelliJ IDEA beépített Mavenje is megfelel.

### Parancssorból

A repository letöltése és a projekt mappájába lépés:

```bash
git clone https://github.com/UriAlexandra/Szoftverteszteles.git
cd Szoftverteszteles/10.19/Mockito
```

A tesztek futtatása:

```bash
mvn clean test
```

A Maven letölti a JUnit 5-öt és a Mockito-t, lefordítja a kódot, és lefuttatja az összes tesztosztályt. A részletes eredmény a `target/surefire-reports` mappába kerül.

**Sikeres futás esetén** a kimenet végén ez látszik:

```
[INFO] Results:
[INFO]
[INFO] Tests run: 56, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

Az 56 teszt megoszlása: `BankServiceTest` 12, `ExchangeRateServiceTest` 8, `BankAccountTest` 33, valamint az órai `UserTest` 2 és `AppTest` 1.

Ha egy teszt elbukik, a `Failures` értéke 0-nál nagyobb lesz, a Maven kiírja a bukott teszt nevét az elvárt és a kapott értékkel, és a végén `BUILD FAILURE` jelenik meg. Egy elmaradt vagy hibás `verify()` esetén a Mockito azt is kiírja, melyik hívást várta, és milyen hívások történtek valójában.

### IntelliJ IDEA-ban

1. **File → Open**, majd a `10.19/Mockito` mappa `pom.xml` fájlja, és megnyitás projektként.
2. Jobb klikk a `src/test/java` mappán, majd **Run 'All Tests'**.

Sikeres futás esetén a Run ablakban zöld pipa és a **„Tests passed: 56 of 56 tests”** felirat jelenik meg.

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

### Órai fájlok

A mappában maradtak a 10.19-es óra fájljai is, ezek is lefutnak a `mvn clean test`-tel:

- **`UserTest`:** annotációs Mockito-használat. A `@Mock` és az `@ExtendWith(MockitoExtension.class)` mellett `when().thenReturn()`, `verify()` és `doThrow()` is szerepel benne (a `User` és a `UserDatabase` osztályhoz).
- **`App` és `AppTest`:** a Maven-projektsablon kiinduló osztálya és tesztje.
