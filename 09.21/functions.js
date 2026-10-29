function arabicToRomanConverter(number) {
  return "I".repeat(number);
}

// Node alatti futtatáshoz (npm test)
if (typeof module !== "undefined" && module.exports) {
  module.exports = { arabicToRomanConverter };
}
