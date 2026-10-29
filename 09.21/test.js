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
});
