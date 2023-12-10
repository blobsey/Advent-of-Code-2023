import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day01 {
    public static void main(String[] args) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader("input01.txt"))) {
            Map<String, Integer> digitsLen3 = Map.of(
                    "one", 1,
                    "two", 2,
                    "six", 6
            );

            Map<String, Integer> digitsLen4 = Map.of(
                    "four", 4,
                    "five", 5,
                    "nine", 9
            );

            Map<String, Integer> digitsLen5 = Map.of(
                    "three", 3,
                    "seven", 7,
                    "eight", 8
            );

            int sum = 0;

            String line;
            boolean shouldBreak = false;

            while ((line = in.readLine()) != null) {
                for (int i = 0; i < line.length(); ++i) {

                    String currentLen3 = (i >= 3) ? line.substring(i - 2, i + 1) : "";
                    String currentLen4 = (i >= 4) ? line.substring(i - 3, i + 1) : "";
                    String currentLen5 = (i >= 5) ? line.substring(i - 4, i + 1) : "";

                    if (shouldBreak = (digitsLen3.containsKey(currentLen3)))
                        sum += 10 * digitsLen3.get(currentLen3);
                    else if (shouldBreak = (digitsLen4.containsKey(currentLen4)))
                        sum += 10 * digitsLen4.get(currentLen4);
                    else if (shouldBreak = (digitsLen5.containsKey(currentLen5)))
                        sum += 10 * digitsLen5.get(currentLen5);
                    else if (shouldBreak = Character.isDigit(line.charAt(i)))
                        sum += 10 * (line.charAt(i) - '0');

                    if (shouldBreak) break;
                }

                for (int j = line.length() - 1; j >= 0; --j) {

                    int len = line.length() - j;

                    String currentLen3 = (len >= 3) ? line.substring(j, j + 3) : "";
                    String currentLen4 = (len >= 4) ? line.substring(j, j + 4) : "";
                    String currentLen5 = (len >= 5) ? line.substring(j, j + 5) : "";

                    if (shouldBreak = (digitsLen3.containsKey(currentLen3)))
                        sum += digitsLen3.get(currentLen3);
                    else if (shouldBreak = (digitsLen4.containsKey(currentLen4)))
                        sum += digitsLen4.get(currentLen4);
                    else if (shouldBreak = (digitsLen5.containsKey(currentLen5)))
                        sum += digitsLen5.get(currentLen5);
                    else if (shouldBreak = Character.isDigit(line.charAt(j)))
                        sum += (line.charAt(j) - '0');

                    if (shouldBreak) break;
                }
            }

            System.out.println("The answer is: " + sum);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}