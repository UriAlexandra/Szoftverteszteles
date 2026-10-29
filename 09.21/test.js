// Node alatti futtatáshoz (npm test) – böngészőben a test.html tölti be a functions.js-t
if (typeof module !== "undefined" && module.exports) {
  globalThis.arabicToRomanConverter =
    require("./functions.js").arabicToRomanConverter;
}

QUnit.module("Arab szám → római szám átalakító", function () {});
