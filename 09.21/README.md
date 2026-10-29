# Arab szám → római szám átalakító (TDD)

Szoftvertesztelés – 2. feladat. JavaScript-függvény, amely az 1 és 3999 közötti egész számokat római számmá alakítja. A fejlesztés tesztvezérelten (TDD) történt, QUnit tesztkeretrendszerrel. A folyamat a Git commitokból követhető.

## Fájlok

| Fájl | Tartalom |
|---|---|
| `functions.js` | Az `arabicToRomanConverter(number)` függvény |
| `test.js` | A QUnit-tesztek |
| `test.html` | Böngészős tesztfuttató (QUnit 2.26.0) |
| `package.json` | Parancssori tesztfuttatás (`npm test`) |

## Működés

```js
arabicToRomanConverter(1);    // "I"
arabicToRomanConverter(58);   // "LVIII"
arabicToRomanConverter(1994); // "MCMXCIV"
arabicToRomanConverter(3999); // "MMMCMXCIX"

arabicToRomanConverter(0);    // RangeError – csak 1 és 3999 között
arabicToRomanConverter(-5);   // RangeError
arabicToRomanConverter(4000); // RangeError
arabicToRomanConverter(1.5);  // TypeError – nem egész szám
arabicToRomanConverter("10"); // TypeError – nem szám típus
```

- **Érvényes bemenet:** egész szám (`number` típus) 1 és 3999 között.
- **Nem egész vagy nem szám bemenet** (pl. `1.5`, `NaN`, `Infinity`, `"10"`, `null`, `undefined`): `TypeError`.
- **Tartományon kívüli egész szám** (pl. `0`, `-1`, `4000`): `RangeError`.

Az átalakítás mohó algoritmussal működik. Egy csökkenő sorrendű érték–jel táblázat a kivonásos alakokat is tartalmazza (CM, CD, XC, XL, IX, IV). A függvény mindig a legnagyobb még beleférő értéket vonja le, és a hozzá tartozó jelet fűzi az eredményhez.

## Futtatás és tesztelés

### Böngészőben

Nyisd meg a `test.html` fájlt egy böngészőben. A QUnit a `code.jquery.com` oldalról töltődik be, ezért internetkapcsolat kell hozzá. Az oldal az összes teszt eredményét mutatja, a sikerteleneket pirossal.

### Parancssorból (Node.js)

```bash
npm install
npm test
```

Az `npm test` a QUnit parancssori futtatóját indítja (`qunit test.js`), és TAP formátumban írja ki az eredményt.

## Tesztek

| QUnit-modul | Mit ellenőriz |
|---|---|
| Normál működés | A feladatleírás összes példája, az alapjelek (I, V, X, L, C, D, M), az ismétléses alakok (pl. III, LXXX, MMMDCCCLXXXVIII) |
| Speciális esetek: kivonásos alakok | IV, IX, XL, XC, CD, CM önállóan és kombinálva (pl. 49 → XLIX, 444 → CDXLIV, 1994 → MCMXCIV) |
| Határértékek | 1 és 3999 még érvényes; 0 és 4000 közvetlenül a határon kívül; negatív és túl nagy számok |
| Érvénytelen bemenetek | Nem egész számok (1.5, -2.5, NaN, Infinity), nem szám típusok ("10", "", null, undefined, true, paraméter nélkül) |
| Teljes tartomány | 1 és 3999 között minden eredmény szabályos római szám, és nincs két azonos |

A kivételeket a tesztek reguláris kifejezéssel ellenőrzik (pl. `assert.throws(fn, /^RangeError/)`). Ennek oka, hogy a QUnit parancssori TAP-riportere elakad, ha egy konstruktorral megadott `assert.throws(fn, RangeError)` elbukik. Böngészőben mindkét forma működik.

## A TDD folyamat

A commitok előtagja jelzi, hogy a ciklus melyik lépése történt:

- **RED:** új, sikertelen teszt
- **GREEN:** a teszt teljesítéséhez szükséges minimális kód
- **REFACTOR:** szerkezeti javítás, a tesztek zöldek maradnak
- **TESZT:** kiegészítő teszt, ami már az első futáskor zöld

| # | RED – új teszt | GREEN – minimális megoldás |
|---|---|---|
| 1 | 1 → I | konstans `"I"` |
| 2 | 2 → II, 3 → III | `"I".repeat(number)` |
| 3 | 5 → V, 6–8 → VI–VIII | `"V"` + I-k ismétlése |
| 4 | 4 → IV, 9 → IX | IV és IX külön esetként |
| – | *REFACTOR:* az if-ágak helyett érték–jel táblázat és mohó algoritmus | |
| 5 | tízesek: X, XL, L, XC | 4 új sor a táblázatban |
| 6 | százasok, ezresek: C, CD, D, CM, M | 5 új sor a táblázatban |
| 7 | határértékek: 0, negatív, 4000+ → RangeError | tartomány-ellenőrzés |
| 8 | nem egész / nem szám bemenet → TypeError | típusellenőrzés a tartomány-ellenőrzés előtt |
| – | *REFACTOR:* konstansok, befagyasztott táblázat, JSDoc; tesztek csoportosítása modulokba | |
| – | *TESZT:* a teljes 1–3999 tartomány ellenőrzése | |

Minden RED commitnál pontosan az újonnan felvett teszt bukik, minden más commitnál az összes teszt zöld. A projekt teljes története a mappában kiadott paranccsal nézhető meg:

```bash
git log --oneline --reverse -- .
```
