function calculate() {
  const target = parseInt(document.getElementById('target').value);
  if (isNaN(target)) {
    alert("Please enter a valid target number.");
    return;
  }

  const inputs = {};
  const letters = "ABCDEFGHIJ";

  // Collect and validate inputs
  for (let ch of letters) {
    const inputValue = document.querySelector(`#${ch} input`).value.trim();
    if (inputValue.length < 2) {
      alert(`Input for ${ch} is invalid. Use format like +5, -3, *2.`);
      return;
    }
    const op = inputValue[0];
    const num = parseInt(inputValue.slice(1));
    if (!['+', '-', '*', '/'].includes(op) || isNaN(num)) {
      alert(`Invalid input for ${ch}. Format must be one operator (+, -, *, /) followed by a number.`);
      return;
    }
    inputs[ch] = { op, val: num };
  }

  const resultsDiv = document.getElementById("results");
  resultsDiv.innerHTML = "<h3>Valid combinations:</h3>";

  let found = false;

  // Generate all combinations of 3 distinct letters
  for (let i = 0; i < letters.length; i++) {
    for (let j = 0; j < letters.length; j++) {
      for (let k = 0; k < letters.length; k++) {
        if (i === j || j === k || i === k) continue;

        const a = letters[i], b = letters[j], c = letters[k];
        const result = evaluate(inputs[a], inputs[b], inputs[c]);

        if (result === target) {
          found = true;
          const eqStr = `${a}${b}${c} → ${inputs[a].val} ${inputs[b].op} ${inputs[b].val} ${inputs[c].op} ${inputs[c].val} = ${result}`;
          const p = document.createElement("p");
          p.textContent = eqStr;
          resultsDiv.appendChild(p);
        }
      }
    }
  }

  if (!found) {
    resultsDiv.innerHTML += "<p>No valid combinations found.</p>";
  }
}

// Evaluate expression a op1 b op2 c with BODMAS (operator precedence)
function evaluate(a, b, c) {
  const precedence = { '+': 1, '-': 1, '*': 2, '/': 2 };

  // Helper function to compute x operator y safely
  function operate(x, operator, y) {
    if (operator === '+') return x + y;
    if (operator === '-') return x - y;
    if (operator === '*') return x * y;
    if (operator === '/') {
      if (y === 0) return null; // avoid divide by zero
      return Math.floor(x / y); // integer division, can change to floating point if needed
    }
    return null;
  }

  const val1 = a.val;
  const op1 = b.op;
  const val2 = b.val;
  const op2 = c.op;
  const val3 = c.val;

  // If op2 has higher precedence than op1, compute b op2 c first, then a op1 result
  if (precedence[op2] > precedence[op1]) {
    const right = operate(val2, op2, val3);
    if (right === null) return null;
    return operate(val1, op1, right);
  } else {
    // Else compute a op1 b first, then result op2 c
    const left = operate(val1, op1, val2);
    if (left === null) return null;
    return operate(left, op2, val3);
  }
}
