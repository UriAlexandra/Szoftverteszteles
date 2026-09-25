function square(x) {
  return x * x;
}

function fizzBuzz(szam) {
  if (
    (szam % 3 == 0 && szam % 5 == 0) ||
    (szam.toString().includes("3") && szam.toString().includes("5")) ||
    (szam.toString().includes("5") && szam % 3 == 0)
  )
    return "FizzBuzz";
  else if (szam % 3 == 0 || szam.toString().includes("3")) return "Fizz";
  else if (szam % 5 == 0 || szam.toString().includes("5")) return "Buzz";
  else return szam;
}

function arabicToRomanConverter(n) {
  if (!n || n <= 0) return "";

  const romanNumerals = [
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

  let result = "";

  for (let i = 0; i < romanNumerals.length; i++) {
    while (n >= romanNumerals[i].value) {
      result += romanNumerals[i].symbol;
      n -= romanNumerals[i].value;
    }
  }

  return result;
}