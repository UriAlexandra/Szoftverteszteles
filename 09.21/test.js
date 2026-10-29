// Node alatti futtatáshoz (npm test) – böngészőben a test.html tölti be a functions.js-t
if (typeof module !== "undefined" && module.exports) {
  globalThis.arabicToRomanConverter =
    require("./functions.js").arabicToRomanConverter;
}

QUnit.module("Arab szám → római szám átalakító", function () {
QUnit.test("Az 1 átalakítása", function (assert) {
    assert.strictEqual(arabicToRomanConverter(1), "I", "1 → I");
  });

QUnit.test("Az I ismétlése: 2 és 3", function (assert) {
    assert.strictEqual(arabicToRomanConverter(2), "II", "2 → II");
    assert.strictEqual(arabicToRomanConverter(3), "III", "3 → III");
  });

QUnit.test("Az V jel és a rá épülő számok: 5–8", function (assert) {
    assert.strictEqual(arabicToRomanConverter(5), "V", "5 → V");
    assert.strictEqual(arabicToRomanConverter(6), "VI", "6 → VI");
    assert.strictEqual(arabicToRomanConverter(7), "VII", "7 → VII");
    assert.strictEqual(arabicToRomanConverter(8), "VIII", "8 → VIII");
  });

QUnit.test("Kivonásos alakok: IV és IX", function (assert) {
    assert.strictEqual(arabicToRomanConverter(4), "IV", "4 → IV");
    assert.strictEqual(arabicToRomanConverter(9), "IX", "9 → IX");
  });

QUnit.test("Tízesek: X, XL, L, XC", function (assert) {
    assert.strictEqual(arabicToRomanConverter(10), "X", "10 → X");
    assert.strictEqual(arabicToRomanConverter(14), "XIV", "14 → XIV");
    assert.strictEqual(arabicToRomanConverter(20), "XX", "20 → XX");
    assert.strictEqual(arabicToRomanConverter(40), "XL", "40 → XL");
    assert.strictEqual(arabicToRomanConverter(49), "XLIX", "49 → XLIX");
    assert.strictEqual(arabicToRomanConverter(50), "L", "50 → L");
    assert.strictEqual(arabicToRomanConverter(58), "LVIII", "58 → LVIII");
    assert.strictEqual(arabicToRomanConverter(90), "XC", "90 → XC");
    assert.strictEqual(arabicToRomanConverter(99), "XCIX", "99 → XCIX");
  });
});
