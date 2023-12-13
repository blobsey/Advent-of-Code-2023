import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {

    private static void expand(List<List<Character>> grid) {
        if (grid.isEmpty() || grid.get(0).isEmpty()) return;

        // expand rows; iterate through rows and insert rows when there is a blank row
        for (int i = 0; i < grid.size(); ++i) {
            if (grid.get(i).contains('#')) continue;
            else grid.set(i, new ArrayList<Character>(Collections.nCopies(grid.get(0).size(), 'r')));
        }

        // expand cols; iterate through cols and insert cols when there is a blank col
        OUTER: for (int j = 0; j < grid.get(0).size(); ++j) {
            for (int i = 0; i < grid.size(); ++i) if (grid.get(i).get(j) == '#') continue OUTER; // skip if not blank col
            for (int i = 0; i < grid.size(); ++i) grid.get(i).set(j, 'c');
        }
    }

    private static void printGrid(List<List<Character>> grid) {
        for (List<Character> row : grid) {
            for (char c : row)
                System.out.print(c);
            System.out.println();
        }
    }

    private static ArrayList<long[]> getGalaxyCoordinates(List<List<Character>> grid, long expansion) {
        int expandedRows = 0, expandedCols = 0;
        ArrayList<long[]> galaxies = new ArrayList<>();

        ROWS: for (int i = 0; i < grid.size(); ++i) {
            expandedCols = 0;
            for (int j = 0; j < grid.get(0).size(); ++j) {
                switch (grid.get(i).get(j)) {
                    case '#':
                        galaxies.add(new long[]{i + (expandedRows * expansion - expandedRows), j + (expandedCols * expansion - expandedCols)});
                        break;
                    case 'c':
                        ++expandedCols;
                        break;
                    case 'r':
                        ++expandedRows;
                        continue ROWS;
                }
            }
        }

        return galaxies;
    }

    public static void main(String[] args) {
        try {
            String line;
            BufferedReader in = new BufferedReader(new FileReader("input/input11.txt"));

            List<List<Character>> grid = new ArrayList<>();

            while((line = in.readLine()) != null) {
                // read lines into grid
                grid.add(line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
            }


            expand(grid);


            // shortest distance between galaxies is just x_distance + y_distance
            // calculate that for both expansion size 2, and expansion size 1mil
            long part1Answer = 0, part2Answer = 0;
            List<long[]> galaxies = getGalaxyCoordinates(grid, 2);
            List<long[]> galaxies2 = getGalaxyCoordinates(grid, 1_000_000);

            for (int i = 0; i < galaxies.size(); ++i) {
                for (int j = i + 1; j < galaxies.size(); ++j) {
                    long[] galaxy1 = galaxies.get(i);
                    long[] galaxy2 = galaxies.get(j);
                    part1Answer += Math.abs(galaxy1[0] - galaxy2[0]) + Math.abs(galaxy1[1] - galaxy2[1]);

                    galaxy1 = galaxies2.get(i);
                    galaxy2 = galaxies2.get(j);
                    part2Answer += Math.abs(galaxy1[0] - galaxy2[0]) + Math.abs(galaxy1[1] - galaxy2[1]);
                }
            }


            System.out.println("The answer to part 1 is: " + part1Answer);
            System.out.println("The answer to part 2 is: " + part2Answer);

        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
