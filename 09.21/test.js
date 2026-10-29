// Node alatti futtatáshoz (npm test) – böngészőben a test.html tölti be a functions.js-t
if (typeof module !== "undefined" && module.exports) {
  globalThis.arabicToRomanConverter =
    require("./functions.js").arabicToRomanConverter;
}

QUnit.module("Arab szám → római szám átalakító", function () {
  QUnit.module("Normál működés", function () {
    QUnit.test.each(
      "A feladatleírás példái",
      {
        "1 → I": [1, "I"],
        "4 → IV": [4, "IV"],
        "9 → IX": [9, "IX"],
        "14 → XIV": [14, "XIV"],
        "58 → LVIII": [58, "LVIII"],
        "1994 → MCMXCIV": [1994, "MCMXCIV"],
        "3999 → MMMCMXCIX": [3999, "MMMCMXCIX"],
      },
      function (assert, [input, expected]) {
        assert.strictEqual(
          arabicToRomanConverter(input),
          expected,
          `${input} → ${expected}`,
        );
      },
    );

    QUnit.test("Az alapjelek: I, V, X, L, C, D, M", function (assert) {
      assert.strictEqual(arabicToRomanConverter(1), "I", "1 → I");
      assert.strictEqual(arabicToRomanConverter(5), "V", "5 → V");
      assert.strictEqual(arabicToRomanConverter(10), "X", "10 → X");
      assert.strictEqual(arabicToRomanConverter(50), "L", "50 → L");
      assert.strictEqual(arabicToRomanConverter(100), "C", "100 → C");
      assert.strictEqual(arabicToRomanConverter(500), "D", "500 → D");
      assert.strictEqual(arabicToRomanConverter(1000), "M", "1000 → M");
    });

    QUnit.test("Ismétléses (összeadásos) alakok", function (assert) {
      assert.strictEqual(arabicToRomanConverter(2), "II", "2 → II");
      assert.strictEqual(arabicToRomanConverter(3), "III", "3 → III");
      assert.strictEqual(arabicToRomanConverter(6), "VI", "6 → VI");
      assert.strictEqual(arabicToRomanConverter(7), "VII", "7 → VII");
      assert.strictEqual(arabicToRomanConverter(8), "VIII", "8 → VIII");
      assert.strictEqual(arabicToRomanConverter(20), "XX", "20 → XX");
      assert.strictEqual(arabicToRomanConverter(30), "XXX", "30 → XXX");
      assert.strictEqual(arabicToRomanConverter(80), "LXXX", "80 → LXXX");
      assert.strictEqual(arabicToRomanConverter(300), "CCC", "300 → CCC");
      assert.strictEqual(arabicToRomanConverter(2026), "MMXXVI", "2026 → MMXXVI");
      assert.strictEqual(
        arabicToRomanConverter(3888),
        "MMMDCCCLXXXVIII",
        "3888 → MMMDCCCLXXXVIII (a leghosszabb alak)",
      );
    });
  });

  QUnit.module("Speciális esetek: kivonásos alakok", function () {
    QUnit.test("Önálló kivonásos alakok", function (assert) {
      assert.strictEqual(arabicToRomanConverter(4), "IV", "4 → IV");
      assert.strictEqual(arabicToRomanConverter(9), "IX", "9 → IX");
      assert.strictEqual(arabicToRomanConverter(40), "XL", "40 → XL");
      assert.strictEqual(arabicToRomanConverter(90), "XC", "90 → XC");
      assert.strictEqual(arabicToRomanConverter(400), "CD", "400 → CD");
      assert.strictEqual(arabicToRomanConverter(900), "CM", "900 → CM");
    });

    QUnit.test("Több kivonásos alak egy számban", function (assert) {
      assert.strictEqual(arabicToRomanConverter(14), "XIV", "14 → XIV");
      assert.strictEqual(arabicToRomanConverter(49), "XLIX", "49 → XLIX");
      assert.strictEqual(arabicToRomanConverter(99), "XCIX", "99 → XCIX");
      assert.strictEqual(arabicToRomanConverter(444), "CDXLIV", "444 → CDXLIV");
      assert.strictEqual(arabicToRomanConverter(999), "CMXCIX", "999 → CMXCIX");
      assert.strictEqual(arabicToRomanConverter(1994), "MCMXCIV", "1994 → MCMXCIV");
    });
  });

  QUnit.module("Határértékek", function () {
    QUnit.test("A tartomány két vége még érvényes", function (assert) {
      assert.strictEqual(arabicToRomanConverter(1), "I", "alsó határ: 1 → I");
      assert.strictEqual(
        arabicToRomanConverter(3999),
        "MMMCMXCIX",
        "felső határ: 3999 → MMMCMXCIX",
      );
    });

    QUnit.test("Közvetlenül a tartományon kívül: 0 és 4000", function (assert) {
      assert.throws(() => arabicToRomanConverter(0), /^RangeError/, "0 → RangeError");
      assert.throws(() => arabicToRomanConverter(4000), /^RangeError/, "4000 → RangeError");
    });

    QUnit.test("Negatív és túl nagy számok", function (assert) {
      assert.throws(() => arabicToRomanConverter(-1), /^RangeError/, "-1 → RangeError");
      assert.throws(() => arabicToRomanConverter(-58), /^RangeError/, "-58 → RangeError");
      assert.throws(() => arabicToRomanConverter(10000), /^RangeError/, "10000 → RangeError");
    });
  });

  QUnit.module("Érvénytelen bemenetek", function () {
    QUnit.test("Nem egész számok", function (assert) {
      assert.throws(() => arabicToRomanConverter(1.5), /^TypeError/, "1.5 → TypeError");
      assert.throws(() => arabicToRomanConverter(3.14), /^TypeError/, "3.14 → TypeError");
      assert.throws(() => arabicToRomanConverter(-2.5), /^TypeError/, "-2.5 → TypeError");
      assert.throws(() => arabicToRomanConverter(NaN), /^TypeError/, "NaN → TypeError");
      assert.throws(() => arabicToRomanConverter(Infinity), /^TypeError/, "Infinity → TypeError");
    });

    QUnit.test("Nem szám típusú bemenetek", function (assert) {
      assert.throws(() => arabicToRomanConverter("10"), /^TypeError/, '"10" (szöveg) → TypeError');
      assert.throws(() => arabicToRomanConverter(""), /^TypeError/, '"" (üres szöveg) → TypeError');
      assert.throws(() => arabicToRomanConverter(null), /^TypeError/, "null → TypeError");
      assert.throws(() => arabicToRomanConverter(undefined), /^TypeError/, "undefined → TypeError");
      assert.throws(() => arabicToRomanConverter(), /^TypeError/, "paraméter nélkül → TypeError");
      assert.throws(() => arabicToRomanConverter(true), /^TypeError/, "true → TypeError");
    });
  });

  QUnit.module("Teljes tartomány", function () {
    QUnit.test("1 és 3999 között minden szám szabályos, egyedi római számot ad", function (assert) {
      const validRoman = /^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/;
      const seen = new Set();
      const invalid = [];
      for (let n = 1; n <= 3999; n++) {
        const roman = arabicToRomanConverter(n);
        if (roman === "" || !validRoman.test(roman)) {
          invalid.push(n);
        }
        seen.add(roman);
      }
      assert.deepEqual(invalid, [], "minden eredmény szabályos római szám");
      assert.strictEqual(seen.size, 3999, "nincs két szám ugyanazzal a római alakkal");
    });
  });
});
