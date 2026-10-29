function arabicToRomanConverter(number) {
  if (number >= 5) {
    return "V" + "I".repeat(number - 5);
  }
  return "I".repeat(number);
}

// Node alatti futtatáshoz (npm test)
if (typeof module !== "undefined" && module.exports) {
  module.exports = { arabicToRomanConverter };
}
