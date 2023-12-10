import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day08 {

    private static long gcd(long a, long b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }
    private static long lcm(long a, long b) {
        return a * (b / Math.abs(gcd(a, b)));
    }
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("input08.txt"));

            String directions = in.readLine();
            String line;

            // Build a graph of source -> dest
            // Ex. "AAA" -> ["BBB", "CCC"]
            HashMap<String, String[]> graph = new HashMap<>();
            ArrayList<String> startPoints = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                try {
                    String[] splitByEquals = line.split("\\s+=\\s+");
                    String source = splitByEquals[0];
                    String[] dest = splitByEquals[1]
                            .replaceAll("[()\\s]", "")
                            .split(",");
                    graph.put(source, dest);

                    if (source.endsWith("A")) startPoints.add(source);

                } catch (ArrayIndexOutOfBoundsException e) { continue; }
            }

            // Print out graph
            for (Map.Entry<String, String[]> entry : graph.entrySet()) {
                System.out.println(entry.getKey() + " " + Arrays.toString(entry.getValue()));
            }

            // Find answer to part 1
            // Follow instructions (repeating if necessary) until end at "ZZZ"

            int steps = 0;
            String current = "AAA";
            try {
                current = "AAA";
                while (!current.equals("ZZZ"))
                    current = directions.charAt(steps++ % directions.length()) == 'L' ?
                            graph.get(current)[0] :
                            graph.get(current)[1];
            } catch (NullPointerException e) {
                System.err.println("Encountered node not in graph: " + current);
                e.printStackTrace();
            }

            System.out.println("The answer to part 1 is: " + steps);

            // Find answer to part 2
            // Iterate through startPoints, follow instructions until all are some "**Z"
            System.out.println(startPoints);
            long[] stepsArray = new long[startPoints.size()];
            current = "";
            try {
                for (int i = 0; i < startPoints.size(); ++i) {
                    current = startPoints.get(i);
                    while (!current.endsWith("Z"))
                        current = directions.charAt((int)stepsArray[i]++ % directions.length()) == 'L' ?
                                graph.get(current)[0] :
                                graph.get(current)[1];
                }
            } catch (NullPointerException e) {
                System.err.println("Encountered node not in graph: " + current);
                e.printStackTrace();
                throw e;
            }

            System.out.println(Arrays.toString(stepsArray));
            // multiply all the steps together to find the lowest common multiple
            long part2Answer = Arrays.stream(stepsArray).reduce(1, Day08::lcm);

            System.out.println("The answer to part 2 is: " + part2Answer);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
