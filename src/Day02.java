import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Day02 {
    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new FileReader("input/input02.txt"))) {
            //Ex line == "Game 1: 19 blue, 12 red; 19 blue, 2 green, 1 red; 13 red, 11 blue"
            String line;
            int sum = 0;
            int sumOfPowers = 0;

            LINE: while ((line = in.readLine()) != null) {
                // Separate Game # from Results
                String[] tmp = line.split(":");
                if (tmp.length != 2) continue;

                String game = tmp[0].trim();
                String results = tmp[1].trim();

                // Parse game number from game
                int gameNum;
                try { gameNum = Integer.parseInt(game.split("\\s+")[1]); }
                catch (NumberFormatException e) { continue; }

                // Split results into subsets
                // ex "12 green, 8 blue, 2 red" == subset
                String[] subsets =
                        Arrays.stream(results.split(";"))
                                .map(String::trim)
                                .toArray(String[]::new);


                int reds = 0, greens = 0, blues = 0;
                int maxReds = 0, maxGreens = 0, maxBlues = 0;

                // Split subsets into number and cubeColor based on "{number} {cubeColor}"
                boolean isPossible = true;
                for (String subset : subsets) {
                    reds = 0;
                    greens = 0;
                    blues = 0;


                    // Split into cubeCounts {number} {color}
                    // ex. cubeCounts[0] == "7 blue"
                    String[] cubeCounts =
                            Arrays.stream(subset.split(","))
                                    .map(String::trim)
                                    .toArray(String[]::new);

                    // Iterate through cubeCounts, split into number color
                    for (String cubeCount : cubeCounts) {
                        String[] tmp2 = cubeCount.split("\\s+");

                        if (tmp2.length != 2) continue LINE;

                        int parsedNum;
                        try { parsedNum = Integer.parseInt(tmp2[0]); }
                        catch (NumberFormatException e) { continue LINE; }
                        String color = tmp2[1].replaceAll("^,+|,+$", "");


                        switch (color) {
                            case "red":
                                reds = parsedNum;
                                maxReds = Math.max(reds, maxReds);
                                break;
                            case "blue":
                                blues = parsedNum;
                                maxBlues = Math.max(blues, maxBlues);
                                break;
                            case "green":
                                greens = parsedNum;
                                maxGreens = Math.max(greens, maxGreens);
                                break;
                            default:
                                continue LINE;
                        }

                    }
                    isPossible = isPossible && (reds <= 12 && greens <= 13 && blues <= 14);

                }

                sum += (isPossible) ? gameNum : 0;
                sumOfPowers += maxReds * maxBlues * maxGreens;

            }
            System.out.println("The answer to part 1 is: " + sum); //2632
            System.out.println("The answer to part 2 is: " + sumOfPowers);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}