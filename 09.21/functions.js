const ROMAN_NUMERALS = [
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
