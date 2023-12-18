import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {

    private final static long CYCLES = 1_000_000_000;
    private static void roll(char[][] grid, int row, int col, int v, int h) {
        grid[row][col] = '.';

        if (v != 0) {
            while (row >= 0 && row < grid.length && grid[row][col] != 'O' && grid[row][col] != '#') row += v;
            row -= v;
        }
        else {
            while (col >= 0 && col < grid[0].length && grid[row][col] != 'O' && grid[row][col] != '#') col += h;
            col -= h;
        }

        grid[row][col] = 'O';

    }

    private static void cycle(char[][] grid) {
        int[][] directions = {
                {-1, 0},
                {0, -1},
                {1, 0},
                {0, 1}
        };

        // north
        for (int i = 0; i < grid.length; ++i)
            for (int j = 0; j < grid[0].length; ++j)
                if (grid[i][j] == 'O')
                        roll(grid, i, j, -1, 0);

        // west
        for (int i = 0; i < grid.length; ++i)
            for (int j = 0; j < grid[0].length; ++j)
                if (grid[i][j] == 'O')
                    roll(grid, i, j, 0, -1);

        // south
        for (int i = grid.length - 1; i >= 0; --i)
            for (int j = 0; j < grid[0].length; ++j)
                if (grid[i][j] == 'O')
                    roll(grid, i, j, 1, 0);

        // east
        for (int i = 0; i < grid.length; ++i)
            for (int j = grid[0].length - 1; j >= 0; --j)
                if (grid[i][j] == 'O')
                    roll(grid, i, j, 0, 1);

        printGrid(grid);

    }

    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char c : row) System.out.print(c);
            System.out.println();
        }
        System.out.println();
    }

    private static int calculateLoad(char[][] grid) {
        int load = 0;
        for (int i = 0; i < grid.length; ++i)
            for (int j = 0; j < grid[0].length; ++j)
                if (grid[i][j] == 'O') load += grid.length - i;
        return load;
    }

    private static String stringify(char[][] grid) {
        return Arrays.stream(grid)
                .map(String::new)
                .collect(Collectors.joining("\n"));
    }

    private static char[][] destringify(String s) {
        return Arrays.stream(s.split("\n")).map(String::toCharArray).toArray(char[][]::new);
    }
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input14.txt"));
            String line;
            List<char[]> tempGrid = new ArrayList<>();
            while((line = in.readLine()) != null) {
                tempGrid.add(line.toCharArray());
            }
            char[][] grid = tempGrid.stream().toArray(char[][]::new);

            for (int i = 0; i < grid.length; ++i)
                for (int j = 0; j < grid[0].length; ++j)
                    if (grid[i][j] == 'O')
                        roll(grid, i, j, -1, 0);



            System.out.println("The answer to part 1 is: " + calculateLoad(grid));


            /* Part 2 */

            // Reset grid to original
            grid = tempGrid.stream().toArray(char[][]::new);

            HashMap<String, Long> gridLookup = new HashMap<>();
            HashMap<Long, String> indexLookup = new HashMap<>();

            long i = 0;
            for (; i < CYCLES; ++i) {
                String gridStr = stringify(grid);
                if (gridLookup.containsKey(gridStr)) break;
                gridLookup.put(gridStr, i);
                indexLookup.put(i, gridStr);
                cycle(grid);
            }

            long start = gridLookup.get(stringify(grid));
            long cycleLength = i - start;
            grid = destringify(indexLookup.get(start + (CYCLES - start) % cycleLength));


            System.out.println("The answer to part 2 is: " + calculateLoad(grid));



        } catch (IOException e) { e.printStackTrace(); }
    }
}
