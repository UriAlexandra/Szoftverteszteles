const ROMAN_NUMERALS = [
  { value: 9, symbol: "IX" },
  { value: 5, symbol: "V" },
  { value: 4, symbol: "IV" },
  { value: 1, symbol: "I" },
];

function arabicToRomanConverter(number) {
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
