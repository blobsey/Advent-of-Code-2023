import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Day09 {

    // recursively loop thru sequences
    private static int recurse(int[] sequence) {
        // base cases are when sequence is len==1 or when all members are equal
        if (sequence.length == 0) return 0;
        if (sequence.length == 1) return sequence[0];
        boolean isBase = true;

        // check if were in the base case, also build up the next sequence array
        int last = sequence.length - 1;
        int[] nextSequence = new int[last];
        for (int i = 1; i < sequence.length; ++i) {
            isBase = isBase && (sequence[i] == sequence[i - 1]);
            nextSequence[i - 1] = sequence[i] - sequence[i-1];
        }

        return isBase ? sequence[last] : sequence[last] + recurse(nextSequence);

    }

    private static int recurse2(int[] sequence) {
        // base cases are when sequence is len==1 or when all members are equal
        if (sequence.length == 0) return 0;
        if (sequence.length == 1) return sequence[0];
        boolean isBase = true;

        // check if were in the base case, also build up the next sequence array
        int last = sequence.length - 1;
        int[] nextSequence = new int[last];
        for (int i = 1; i < sequence.length; ++i) {
            isBase = isBase && (sequence[i] == sequence[i - 1]);
            nextSequence[i - 1] = sequence[i] - sequence[i-1];
        }

        return isBase ? sequence[0] : sequence[0] - recurse2(nextSequence);

    }
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input09.txt"));
            String line;
            ArrayList<Integer> extrapolations = new ArrayList<>();
            ArrayList<Integer> extrapolations2 = new ArrayList<>();

            while ((line = in.readLine()) != null) {
                // read each line in as an array of ints
                int[] sequence = Arrays.stream(line.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();

                // add each extrapolated value to an arraylist
                extrapolations.add(recurse(sequence));
                extrapolations2.add(recurse2(sequence));
            }

            // sum up all the extrapolations
            int part1Answer = extrapolations.stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            int part2Answer = extrapolations2.stream()
                    .mapToInt(Integer::intValue)
                    .sum();


            System.out.println("The answer to part 1 is: " + part1Answer);
            System.out.println("The answer to part 2 is: " + part2Answer);
        } catch (IOException e) { e.printStackTrace(); }

    }
}
