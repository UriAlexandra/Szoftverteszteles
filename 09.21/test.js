QUnit.module("Arabic numbers to Roman Converter test cases", function () {
  QUnit.test("Edge cases and invalid inputs", function (assert) {
    assert.equal(
      arabicToRomanConverter(),
      "",
      "Undefined/No argument should return empty string",
    );
    assert.equal(arabicToRomanConverter(0), "", "0 should return empty string");
    assert.equal(
      arabicToRomanConverter(-5),
      "",
      "Negative numbers should return empty string",
    );
  });

  QUnit.test("Base symbols", function (assert) {
    assert.equal(arabicToRomanConverter(1), "I", "1 should return I");
    assert.equal(arabicToRomanConverter(5), "V", "5 should return V");
    assert.equal(arabicToRomanConverter(10), "X", "10 should return X");
    assert.equal(arabicToRomanConverter(50), "L", "50 should return L");
    assert.equal(arabicToRomanConverter(100), "C", "100 should return C");
    assert.equal(arabicToRomanConverter(500), "D", "500 should return D");
    assert.equal(arabicToRomanConverter(1000), "M", "1000 should return M");
  });

  QUnit.test("Subtractive notation (IV, IX, XL, etc.)", function (assert) {
    assert.equal(arabicToRomanConverter(4), "IV", "4 should return IV");
    assert.equal(arabicToRomanConverter(9), "IX", "9 should return IX");
    assert.equal(arabicToRomanConverter(40), "XL", "40 should return XL");
    assert.equal(arabicToRomanConverter(90), "XC", "90 should return XC");
    assert.equal(arabicToRomanConverter(400), "CD", "400 should return CD");
    assert.equal(arabicToRomanConverter(900), "CM", "900 should return CM");
  });

  QUnit.test("Additive notation sequences", function (assert) {
    assert.equal(arabicToRomanConverter(2), "II", "2 should return II");
    assert.equal(arabicToRomanConverter(3), "III", "3 should return III");
    assert.equal(arabicToRomanConverter(7), "VII", "7 should return VII");
    assert.equal(arabicToRomanConverter(8), "VIII", "8 should return VIII");
    assert.equal(arabicToRomanConverter(20), "XX", "20 should return XX");
    assert.equal(arabicToRomanConverter(30), "XXX", "30 should return XXX");
    assert.equal(arabicToRomanConverter(80), "LXXX", "80 should return LXXX");
  });

  QUnit.test("Complex and historical numbers", function (assert) {
    assert.equal(
      arabicToRomanConverter(1066),
      "MLXVI",
      "1066 should return MLXVI",
    );
    assert.equal(
      arabicToRomanConverter(1776),
      "MDCCLXXVI",
      "1776 should return MDCCLXXVI",
    );
    assert.equal(
      arabicToRomanConverter(1918),
      "MCMXVIII",
      "1918 should return MCMXVIII",
    );
    assert.equal(
      arabicToRomanConverter(1954),
      "MCMLIV",
      "1954 should return MCMLIV",
    );
    assert.equal(
      arabicToRomanConverter(1984),
      "MCMLXXXIV",
      "1984 should return MCMLXXXIV",
    );
    assert.equal(
      arabicToRomanConverter(1999),
      "MCMXCIX",
      "1999 should return MCMXCIX",
    );
  });

  QUnit.test("Modern years", function (assert) {
    assert.equal(arabicToRomanConverter(2000), "MM", "2000 should return MM");
    assert.equal(arabicToRomanConverter(2001), "MMI", "2001 should return MMI");
    assert.equal(
      arabicToRomanConverter(2004),
      "MMIV",
      "2004 should return MMIV",
    );
    assert.equal(
      arabicToRomanConverter(2023),
      "MMXXIII",
      "2023 should return MMXXIII",
    );
    assert.equal(
      arabicToRomanConverter(2024),
      "MMXXIV",
      "2024 should return MMXXIV",
    );
  });

  QUnit.test("Maximum standard Roman numeral", function (assert) {
    assert.equal(
      arabicToRomanConverter(3999),
      "MMMCMXCIX",
      "3999 should return MMMCMXCIX",
    );
  });
});