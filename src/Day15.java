import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Day15 {

    private static int hash(String s) {
        int total = 0;
        for (char c : s.toCharArray()) total = (total + (int)c) * 17 % 256;
        return total;
    }

    public static void main(String[] args) {
        String[] steps = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input15.txt"));
            steps = in.readLine().split(",");
        } catch (IOException e) { e.printStackTrace(); }

        steps = (steps == null) ? new String[0] : steps;

        int part1Answer = Arrays.stream(steps).mapToInt(Day15::hash).sum();

        System.out.println("The answer to part 1 is: " + part1Answer);

        /* Part 2 */

        @SuppressWarnings("unchecked")
        Map<String, Integer>[] hashmap = new LinkedHashMap[256];
        for (int i = 0; i < 256; ++i) hashmap[i] = new LinkedHashMap<>();

        for (String step : steps) {
            String[] temp = step.split("(?=-|=\\d$)");
            String label = temp[0];
            String instruction = temp[1];
            int i = hash(label);
            if (instruction.equals("-")) hashmap[i].remove(label); // if ends with '-' remove
            else hashmap[i].put(label, instruction.charAt(1) - '0'); // if ends with '=d' (d = digit) add/update
        }

        int part2Answer = 0;

        for (int i = 0; i < 256; ++i) {
            int j = 0;
            for (Map.Entry<String, Integer> entry : hashmap[i].entrySet())
                part2Answer += (i + 1) * (++j) * entry.getValue();
        }


        System.out.println("The answer to part 2 is: " + part2Answer);

    }
}
