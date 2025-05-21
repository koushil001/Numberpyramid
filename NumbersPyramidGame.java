package numberpyramid;

import java.util.*;

public class NumbersPyramidGame {

    static class SymbolData {
        char operator;
        int value;

        SymbolData(char operator, int value) {
            this.operator = operator;
            this.value = value;
        }
    }

    // Evaluate expression like A op B op C, following BODMAS
    public static int evaluate(SymbolData a, SymbolData b, SymbolData c) {
        // Apply BODMAS (*/ before +-)
        // First, convert to numbers and operators
        int[] nums = {a.value, b.value, c.value};
        char[] ops = {b.operator, c.operator}; // ignore a.operator

        // First handle *, /
        for (int i = 0; i < 2; i++) {
            if (ops[i] == '*') {
                nums[i] = nums[i] * nums[i + 1];
                // shift elements
                nums[i + 1] = 0;
                ops[i] = '+'; // neutralize after multiplication
            } else if (ops[i] == '/') {
                if (nums[i + 1] == 0) return Integer.MIN_VALUE; // avoid /0
                nums[i] = nums[i] / nums[i + 1];
                nums[i + 1] = 0;
                ops[i] = '+'; // neutralize
            }
        }

        // Now handle + and -
        int result = nums[0];
        for (int i = 0; i < 2; i++) {
            if (ops[i] == '+') result += nums[i + 1];
            else if (ops[i] == '-') result -= nums[i + 1];
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input
        Map<Character, SymbolData> map = new HashMap<>();
        System.out.println("Enter 9 symbols with operator and number (e.g., A + 5):");
        for (int i = 0; i < 9; i++) {
            char label = (char) ('A' + i);
            char op = sc.next().charAt(0);
            int val = sc.nextInt();
            map.put(label, new SymbolData(op, val));
        }

        System.out.print("Enter target number: ");
        int target = sc.nextInt();

        // Generate all 3-letter combinations from A-I
        List<String> validCombos = new ArrayList<>();
        char[] symbols = "ABCDEFGHI".toCharArray();

        for (int i = 0; i < symbols.length; i++) {
            for (int j = 0; j < symbols.length; j++) {
                for (int k = 0; k < symbols.length; k++) {
                    if (i == j || j == k || i == k) continue; // skip duplicates

                    char a = symbols[i], b = symbols[j], c = symbols[k];
                    SymbolData sd1 = map.get(a); // 🔧 Extract values
                    SymbolData sd2 = map.get(b);
                    SymbolData sd3 = map.get(c);

                    int result = evaluate(sd1, sd2, sd3);

                    if (result == target) {
                        // ✅ Construct and print full equation
                        String equation = "" + a + b + c + " → " + sd1.value + " " + sd2.operator + " " + sd2.value + " " + sd3.operator + " " + sd3.value + " = " + result;
                        validCombos.add(equation); // 🔧 Add the full equation to the list
                    }
                }
            }
        }

        // ✅ Output equations instead of just combos
        System.out.println("Valid combinations:");
        for (String combo : validCombos) {
            System.out.println(combo); // ✅ Will print full equation like ABC → 9 + 7 - 2 = 14
        }

        sc.close();
    }
}
