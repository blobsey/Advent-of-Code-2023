import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day06 {
    public static void main(String[] args) {
//        String line;
        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input06.txt"));
            int[] times = Arrays.stream(in.readLine().split("Time:\\s+")[1].split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            int[] distances = Arrays.stream(in.readLine().split("Distance:\\s+")[1].split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            long timeP2 = Long.parseLong(
                    Arrays.stream(times)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining()));

            long distanceP2 = Long.parseLong(
                    Arrays.stream(distances)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining()));


            double[] part1 = new double[times.length];

            for (int i = 0; i < times.length; ++i) {
                double point1 = (times[i] + (Math.sqrt(Math.pow(times[i], 2) - (4.0 * distances[i])))) / 2.0;
                double point2 = (times[i] - (Math.sqrt(Math.pow(times[i], 2) - (4.0 * distances[i])))) / 2.0;
                part1[i] = Math.ceil(point1 - 1) - Math.floor(point2 + 1) + 1;
            }

            double part1Answer = 1;
            for (double i : part1) part1Answer *= i;

            System.out.println("The answer to part 1 is: " + part1Answer);

            double point1 = (timeP2 + (Math.sqrt(Math.pow(timeP2, 2) - (4.0 * distanceP2)))) / 2.0;
            double point2 = (timeP2 - (Math.sqrt(Math.pow(timeP2, 2) - (4.0 * distanceP2)))) / 2.0;
            double part2Answer = Math.ceil(point1 - 1) - Math.floor(point2 + 1) + 1;

            System.out.println("The answer to part 2 is: " + String.format("%.0f", part2Answer));
        }
        catch (IOException e) { e.printStackTrace(); }


    }
}