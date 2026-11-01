# BankAccount – bankszámla JUnit 5 tesztekkel

Szoftvertesztelés – 3. feladat. Egy egyszerű bankszámlát modellező Java-osztály (`BankAccount`) és a működését ellenőrző JUnit 5 egységtesztek.

## Projekt felépítése

```
10.05/Órai jegyzet/BankAccount/
├── pom.xml                                   Maven-konfiguráció (Java 17, JUnit 5.11)
├── src/main/java/org/example/BankAccount.java
└── src/test/java/org/example/BankAccountTest.java
```

## Az osztály

| Metódus | Működés |
|---|---|
| `BankAccount()` | Új számla, az induló egyenleg 0. |
| `void deposit(double amount)` | Befizetés, növeli az egyenleget. |
| `void withdraw(double amount)` | Pénzfelvétel, csökkenti az egyenleget. |
| `double getBalance()` | Visszaadja az aktuális egyenleget. |

**Hibás művelet esetén `IllegalArgumentException` kivételt dob:**
- **0 vagy negatív összeg:** befizetésnél és pénzfelvételnél is hiba.
- **Nem véges összeg (`NaN`, végtelen):** befizetésnél és pénzfelvételnél is hiba. Az egyszerű `amount <= 0` ellenőrzés a `NaN`-t átengedné, és az egyenleg `NaN` lenne.
- **Az egyenlegnél nagyobb pénzfelvétel:** hiba. A teljes egyenleg felvehető.

Minden ellenőrzés az egyenleg módosítása előtt fut le, ezért sikertelen művelet után az egyenleg nem változik.

## A követelmények teljesítése

| Követelmény | Megvalósítás | Teszt |
|---|---|---|
| Az induló egyenleg 0 | a konstruktor 0-ra állítja | 1. eset |
| A `deposit()` növeli az egyenleget | `balance += amount` | 2., 3. eset |
| A `withdraw()` csökkenti az egyenleget | `balance -= amount` | 4., 5. eset |
| A `getBalance()` az aktuális egyenleget adja | visszaadja a `balance` mezőt | minden teszt ezen keresztül ellenőriz |
| 0 vagy negatív befizetés nem engedélyezett | `IllegalArgumentException` | 7., 8. eset és paraméterezett teszt |
| 0 vagy negatív pénzfelvétel nem engedélyezett | `IllegalArgumentException` | 9., 10. eset és paraméterezett teszt |
| Nem vehető fel több, mint az egyenleg | `IllegalArgumentException` | 6. eset, üres számla, paraméterezett teszt |
| Hibás műveletnél `IllegalArgumentException` | minden hibás esetben ez a kivétel | 6–10. eset, hibaüzenetek |
| Sikertelen műveletnél az egyenleg nem változik | az ellenőrzés a módosítás előtt fut | 6–10. eset és „Sikertelen műveletek után az egyenleg változatlan” csoport |

## Futtatás és tesztelés

**Szükséges:** JDK 17 vagy újabb, valamint Maven 3.9+. Ehelyett az IntelliJ IDEA beépített Mavenje is megfelel.

### Parancssorból

A repository letöltése és a projekt mappájába lépés (a mappanévben szóköz van, ezért kell az idézőjel):

```bash
git clone https://github.com/UriAlexandra/Szoftverteszteles.git
cd "Szoftverteszteles/10.05/Órai jegyzet/BankAccount"
```

A tesztek futtatása:

```bash
mvn test
```

A Maven letölti a JUnit 5-öt, lefordítja a kódot, és lefuttatja a teszteket. A részletes eredmény a `target/surefire-reports` mappába kerül.

**Sikeres futás esetén** a kimenet végén ez látszik:

```
[INFO] Results:
[INFO]
[INFO] Tests run: 33, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

Ha egy teszt elbukik, a `Failures` értéke 0-nál nagyobb lesz, a Maven kiírja a bukott teszt nevét az elvárt és a kapott értékkel, és a végén `BUILD FAILURE` jelenik meg.

### IntelliJ IDEA-ban

1. **File → Open**, majd a `10.05/Órai jegyzet/BankAccount` mappa `pom.xml` fájlja, és megnyitás projektként.
2. Jobb klikk a `BankAccountTest` osztályon, majd **Run 'BankAccountTest'**.

Sikeres futás esetén a Run ablakban zöld pipa és a **„Tests passed: 33 of 33 tests”** felirat jelenik meg. A tesztek a beágyazott csoportok szerint, olvasható magyar nevükkel látszanak (pl. „1. Új számla egyenlege 0”).

### Az osztály kipróbálása

A `BankAccount` osztálykönyvtár, nincs `main` metódusa. A tesztek mellett JShell-ből is kipróbálható:

```bash
mvn compile
jshell --class-path target/classes
```

```java
jshell> var a = new org.example.BankAccount();
jshell> a.deposit(1000); a.withdraw(250);
jshell> a.getBalance()
$4 ==> 750.0
jshell> a.withdraw(5000)
|  Exception java.lang.IllegalArgumentException: Nincs elegendő fedezet a számlán!
```

## Tesztek

A tesztek összesen 33 esetet futtatnak, beágyazott (`@Nested`) csoportokban.

**A feladatban előírt esetek.** Számozott sorrendben futnak, `@Order` szerint.

| # | Eset | Ellenőrzés |
|---|---|---|
| 1 | Új számla egyenlege | 0 |
| 2 | `deposit(1000)` | az egyenleg 1000 |
| 3 | 500 + 1500 + 200 befizetés | 2200 |
| 4 | 2000 befizetés, 500 felvétel | 1500 |
| 5 | 1000 befizetés, 1000 felvétel | 0, a teljes egyenleg felvehető |
| 6 | 500 egyenlegnél 600 felvétele | `IllegalArgumentException`, az egyenleg 500 marad |
| 7 | `deposit(-100)` | `IllegalArgumentException`, az egyenleg változatlan |
| 8 | `deposit(0)` | `IllegalArgumentException`, az egyenleg változatlan |
| 9 | `withdraw(-200)` | `IllegalArgumentException`, az egyenleg változatlan |
| 10 | `withdraw(0)` | `IllegalArgumentException`, az egyenleg változatlan |

**Sikertelen műveletek után az egyenleg változatlan.** 1000-es kezdő egyenleggel futnak.
- **Paraméterezett teszt minden hibás befizetésre:** -1000, -100, -0.01, 0, NaN, -∞, +∞.
- **Paraméterezett teszt minden hibás pénzfelvételre:** -1000, -0.01, 0, 1000.01, 1001, 5000, NaN, -∞, +∞.
- **Üres számla:** a legkisebb összeg sem vehető fel róla.
- **Egymás utáni hibák:** több sikertelen művelet egymás után sem változtat az egyenlegen.
- **Használhatóság hiba után:** egy sikertelen művelet után a számla tovább használható.

**További esetek.**
- **Műveletsorok:** több egymás utáni pénzfelvétel, illetve váltakozó befizetések és felvételek.
- **Tört összegek:** fillérek kezelése, a lebegőpontos pontatlanság miatt `assertEquals(expected, actual, delta)` formában.
- **Hibaüzenetek:** a kivételek üzenete beszédes.

## Megjegyzés

A feladat `double` típust ír elő. Valódi pénzügyi alkalmazásban `BigDecimal`-t vagy egész fillérértéket használnánk, mert a `double` nem tud minden tizedes törtet pontosan ábrázolni (pl. 0,1 + 0,2 ≠ 0,3). Ezért hasonlítják a tesztek az egyenleget kis tűréssel (`DELTA = 1e-9`).
