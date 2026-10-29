/** Az átalakítható számok tartománya. */
const MIN_VALUE = 1;
const MAX_VALUE = 3999;

/**
 * Az értékek csökkenő sorrendben, a kivonásos alakokkal (CM, CD, XC, XL, IX, IV) együtt.
 * A mohó algoritmus mindig a legnagyobb még beleférő értéket veszi.
 */
const ROMAN_NUMERALS = Object.freeze([
  { value: 1000, symbol: "M" },
  { value: 900, symbol: "CM" },
  { value: 500, symbol: "D" },
  { value: 400, symbol: "CD" },
  { value: 100, symbol: "C" },
  { value: 90, symbol: "XC" },
  { value: 50, symbol: "L" },
  { value: 40, symbol: "XL" },
  { value: 10, symbol: "X" },
  { value: 9, symbol: "IX" },
  { value: 5, symbol: "V" },
  { value: 4, symbol: "IV" },
  { value: 1, symbol: "I" },
]);

/**
 * Arab számot alakít át római számmá.
 *
 * @param {number} number 1 és 3999 közötti egész szám
 * @returns {string} a szám római alakja
 * @throws {TypeError} ha a bemenet nem egész szám
 * @throws {RangeError} ha a szám nincs 1 és 3999 között
 */
function arabicToRomanConverter(number) {
  if (typeof number !== "number" || !Number.isInteger(number)) {
    throw new TypeError("A bemenetnek egész számnak kell lennie.");
  }
  if (number < MIN_VALUE || number > MAX_VALUE) {
    throw new RangeError(
      `A szám csak ${MIN_VALUE} és ${MAX_VALUE} között lehet.`,
    );
  }

  let rest = number;
  let result = "";
  for (const { value, symbol } of ROMAN_NUMERALS) {
    while (rest >= value) {
      result += symbol;
      rest -= value;
    }
  }
  return result;
}

// Node alatti futtatáshoz (npm test)
if (typeof module !== "undefined" && module.exports) {
  module.exports = { arabicToRomanConverter };
}
