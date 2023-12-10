import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day03 {
    private static char[][] schematic;
    private static int answer_p1;
    private static int answer_p2;

    private static int[][] neighbors;
    static {
        neighbors = new int[][]{
            {-1, -1}, {-1,  0}, {-1,  1},
            { 0, -1},           { 0,  1},
            { 1, -1}, { 1,  0}, { 1,  1}
        };
    }

    private static void readGrid() {
        List<char[]> lines = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader("input/input03.txt"))) {
            String line;
            while ((line = in.readLine()) != null) lines.add(line.toCharArray());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        schematic = lines.toArray(new char[lines.size()][]);
    }

    // Reads a number from a grid regardless of starting
    // point in the number, then replace with .
    private static int takeNumber(int i, int j) {
        // Find start of the number
        while (--j >= 0 && Character.isDigit(schematic[i][j]));

        // Build result left to right, replace with '.' incrementally
        int result = 0;
        while (++j < schematic[0].length && Character.isDigit(schematic[i][j])) {
            result = result*10 + (schematic[i][j] - '0'); //convert char to int, add to result
            schematic[i][j] = '!';
        }

        return result;
    }

    private static void replaceNumber(int i, int j, int num) {
        // Find end of the number
        while (++j < schematic[0].length && schematic[i][j] == '!');

        // Replace chars
        while (--j >= 0 && schematic[i][j] == '!') {
            schematic[i][j] = (char) ((num % 10) + '0');
            num %= 10;
        }
    }

    private static void check(int i, int j) {
        for (int[] n : neighbors) {
            int ni = i + n[0];
            int nj = j + n[1];

            if (    ni >= 0 &&
                    ni < schematic.length &&
                    nj >= 0 &&
                    nj < schematic[0].length &&
                    Character.isDigit(schematic[ni][nj])    )
                answer_p1 += takeNumber(ni, nj);

        }
    }

    private static void checkGear(int i, int j) {
        answer_p2 += backtrack(i, j, 0, new LinkedList<Integer>());
        return;
    }

    private static int backtrack(int i, int j, int n, LinkedList<Integer> adjacentParts) {
        int ni = i + neighbors[n][0];
        int nj = j + neighbors[n][1];

        boolean tookNumber = false;

        if (ni >= 0 && ni < schematic.length && nj >= 0 && nj < schematic[0].length && Character.isDigit(schematic[ni][nj])) {
            adjacentParts.add(takeNumber(ni, nj));
            tookNumber = true;
        }

        if (n == neighbors.length - 1) {
            if (adjacentParts.size() != 2) return 0;
            else return adjacentParts.get(0) * adjacentParts.get(1);
        }

        int result = backtrack(i, j, n + 1, adjacentParts);
        if (tookNumber) replaceNumber(ni, nj, adjacentParts.removeLast());

        return result;

    }

    public static void main(String[] args) {

        readGrid();

        for (int i = 0; i < schematic.length; ++i)
            for (int j = 0; j < schematic[0].length; ++j)
                if (!Character.isDigit(schematic[i][j]) && schematic[i][j] != '.')
                    check(i, j);



        readGrid();

        for (int i = 0; i < schematic.length; ++i)
            for (int j = 0; j < schematic[0].length; ++j)
                if (!Character.isDigit(schematic[i][j]) && schematic[i][j] != '.')
                    checkGear(i, j);


        System.out.println("The answer to part 1 is: " + answer_p1);

        System.out.println("The answer to part 2 is: " + answer_p2);



    }
}