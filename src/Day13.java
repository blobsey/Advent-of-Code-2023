import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {

    public static void printGrids(List<char[][]> grids) {
        for (char[][] grid : grids) {
            for (char[] row : grid) {
                for (char c : row) {
                    System.out.print(c);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static boolean areRowsEqual(char[][] grid, int current, int mirror) {
        return Arrays.equals(grid[current], grid[mirror]);
    }

    public static boolean areColsEqual(char[][] grid, int current, int mirror) {
        for (int i = 0; i < grid.length; ++i)
            if (grid[i][current] != grid[i][mirror]) return false;
        return true;
    }

    private static int countRowSmudges(char[][] grid, int current, int mirror) {
        int smudges = 0;
        for (int i = 0; i < grid[current].length; ++i)
            if (grid[current][i] != grid[mirror][i])
                ++smudges;
        return smudges;
    }

    private static int countColSmudges(char[][] grid, int current, int mirror) {
        int smudges = 0;
        for (int i = 0; i < grid.length; ++i)
            if (grid[i][current] != grid[i][mirror])
                ++smudges;
        return smudges;
    }


    /*
    Starting from the middle, check outwards to see if rows match
    if any dont match, return false
    if we hit a wall, that means all of them matched and we return true
     */
    public static boolean isMirrorCenterRow(char[][] grid, int row) {
        int distance = 0;
        while (true) {
            int i = row + distance, j = row - distance - 1;
            if (i >= grid.length || j <= -1) return true;
            if (!areRowsEqual(grid, i, j)) return false;
            ++distance;
        }
    }

    /*
    Checks middle-out and counts the number of differences
    if smudges is exactly one, return true
    return false otherwise
    */
    public static boolean isSmudgedMirrorRow(char[][] grid, int row) {
        int distance = 0, smudges = 0;
        while (true) {
            int i = row + distance, j = row - distance - 1;
            if (i >= grid.length || j <= -1) return (smudges == 1);
            smudges += countRowSmudges(grid, i, j);
            if (smudges > 1) return false;
            ++distance;
        }
    }

    /*
    Starting from column col, count the amount of smudges in a middle-out fashion
    If there are exactly 1 smudges, return true
    Otherwise, return false
     */
    private static boolean isSmudgedMirrorCol(char[][] grid, int col) {
        int distance = 0, smudges = 0;
        while (true) {
            int i = col + distance, j = col - distance - 1;
            if (i >= grid[0].length || j <= -1) return (smudges == 1);
            smudges += countColSmudges(grid, i, j);
            if (smudges > 1) return false;
            ++distance;
        }
    }

    /*
    Starting from the middle, check outwards to see if cols match
    if any dont match, return false
    if we hit a wall, that means all of them matched and we return true
     */
    public static boolean isMirrorCenterCol(char[][] grid, int col) {
        int distance = 0;
        while (true) {
            int i = col + distance, j = col - distance - 1;
            if (i >= grid[0].length || j <= -1) return true;
            if (!areColsEqual(grid, i, j)) return false;
            ++distance;
        }
    }

    public static void main(String[] args) {
        try {
            /* Read in array of strings */
            BufferedReader in = new BufferedReader(new FileReader("input/input13.txt"));
            String line;
            List<char[][]> grids = new ArrayList<char[][]>();
            List<char[]> tempGrid = new ArrayList<>();
            while ((line = in.readLine()) != null) {
                if (!line.isBlank())
                    tempGrid.add(line.toCharArray());
                else {
                    grids.add(tempGrid.stream().toArray(char[][]::new));
                    tempGrid = new ArrayList<>();
                }
            }
            grids.add(tempGrid.stream().toArray(char[][]::new));

            printGrids(grids);


            int part1Answer = 0;

            /* Part 1 */
            NEXT_GRID: for (char[][] grid : grids) {
                // Check rows
                int rows = 1;
                for (; rows < grid.length; ++rows) {
                    if (isMirrorCenterRow(grid, rows)) {
                        part1Answer += 100 * rows;
                        continue NEXT_GRID;
                    }


                }

                // check cols
                int cols = 1;
                for (; cols < grid[0].length; ++cols)
                    if (isMirrorCenterCol(grid, cols)) {
                        part1Answer += cols;
                        continue NEXT_GRID;
                    }
            }

            System.out.println("The answer to part 1 is: " + part1Answer);

            /* Part 2 */
            int part2Answer = 0;
            NEXT_GRID: for (char[][] grid : grids) {
                // Check rows
                int rows = 1;
                for (; rows < grid.length; ++rows) {
                    if (isSmudgedMirrorRow(grid, rows)) {
                        part2Answer += 100 * rows;
                        continue NEXT_GRID;
                    }
                }

                // Check cols
                int cols = 1;
                for (; cols < grid[0].length; ++cols)
                    if (isSmudgedMirrorCol(grid, cols)) {
                        part2Answer += cols;
                        continue NEXT_GRID;
                    }
            }

            System.out.println("The answer to part 2 is: " + part2Answer);

        }
        catch (IOException e) {e.printStackTrace();}
    }
}
