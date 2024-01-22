import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day18 {

    private static char[][] grid;
    private enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    private static class Instruction {
        public Direction direction;
        public int distance;
        public int hexCode;

        public Instruction(Direction direction, int distance, int hexCode) {
            this.direction = direction;
            this.distance = distance;
            this.hexCode = hexCode;
        }

        @Override
        public String toString() {
            return "Instruction{" +
                    "direction=" + direction +
                    ", distance=" + distance +
                    ", hexCode=" + String.format("#%06X", hexCode) +
                    '}';
        }
    }

    private static void printGrid() {
        for (char[] row : grid) {
            for (char b : row) {
                System.out.print(b);
            }
                System.out.println();
        }
        System.out.println();
    }


    private static void generateGrid(Instruction[] instructions) {
        // determine bounds of grid

        int currenti = 0, currentj = 0, mini = 0, maxi = 0, minj = 0, maxj = 0;

        for (Instruction o : instructions) {
            switch (o.direction) {
                case UP:
                    currenti -= o.distance;
                    mini = Math.min(mini, currenti);
                    break;
                case DOWN:
                    currenti += o.distance;
                    maxi = Math.max(maxi, currenti);
                    break;
                case LEFT:
                    currentj -= o.distance;
                    minj = Math.min(minj, currentj);
                    break;
                case RIGHT:
                    currentj += o.distance;
                    maxj = Math.max(maxj, currentj);
                    break;
            }
        }

        grid = new char[maxi - mini + 3][maxj - minj + 3];
        for (int i = 0; i < grid.length; ++i)
            for (int j = 0; j < grid[0].length; ++j)
                grid[i][j] = '.';

        // (-mini, -minj) is the new (0, 0)
        currenti = -mini + 1;
        currentj = -minj + 1;

        for (Instruction o : instructions) {
            switch (o.direction) {
                case UP:
                    for (int k = 0; k < o.distance; ++k) grid[currenti--][currentj] = '#';
                    break;
                case DOWN:
                    for (int k = 0; k < o.distance; ++k) grid[currenti++][currentj] = '#';
                    break;
                case LEFT:
                    for (int k = 0; k < o.distance; ++k) grid[currenti][currentj--] = '#';
                    break;
                case RIGHT:
                    for (int k = 0; k < o.distance; ++k) grid[currenti][currentj++] = '#';
                    break;
            }
        }

        Queue<int[]> bfs = new LinkedList<>();
        bfs.add(new int[]{maxi - mini + 1, maxj - minj + 1});
        int[][] offsets = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while(!bfs.isEmpty()) {
            int[] step = bfs.poll();
            int i = step[0];
            int j = step[1];


            for (int[] offset : offsets) {
                int nexti = i + offset[0];
                int nextj = j + offset[1];

                // bounds check
                if (nexti < 0 || nexti >= grid.length || nextj < 0 || nextj >= grid[0].length)
                    continue;

                if (grid[nexti][nextj] == '.') {
                    grid[nexti][nextj] = 'o';
                    bfs.add(new int[]{nexti, nextj});
                }

            }

        }

        for (int i = 0; i < grid.length; ++i)
            for (int j = 0; j < grid[0].length; ++j)
                grid[i][j] = (grid[i][j] == 'o') ? '.' : '#';
    }

    /* Part 2 */

    private static class Interval implements Comparable<Interval> {
        int startj;
        int endj;

        public Interval(int startj, int endj) {
            this.startj = startj;
            this.endj = endj;
        }

        @Override
        public String toString() {
            return  "[" + startj + " -> " + endj + "]";
        }

        @Override
        public int compareTo(Interval o) {
            return Integer.compare(this.startj, o.startj);
        }
    }


    public static void main(String[] args) {
        Instruction[] instructions;
        ArrayList<Instruction> temp = new ArrayList<>();


        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input18.txt"));
            String line;
            while ((line = in.readLine()) != null) {
                String[] splitBySpace = line.split("\\s+");
                if (splitBySpace.length != 3) continue;

                Direction direction = Direction.NONE;
                switch(splitBySpace[0]) {
                    case "U": direction = Direction.UP; break;
                    case "D": direction = Direction.DOWN; break;
                    case "L": direction = Direction.LEFT; break;
                    case "R": direction = Direction.RIGHT; break;
                }

                int distance = Integer.parseInt(splitBySpace[1]);

                int hexCode = Integer.parseInt(splitBySpace[2].replaceAll("[^0-9A-Fa-f]", ""), 16);

                temp.add(new Instruction(direction, distance, hexCode));
            }
        } catch (IOException e) { e.printStackTrace(); }

        instructions = temp.toArray(Instruction[]::new);

        generateGrid(instructions);

        int part1Answer = 0;

        for (int i = 0; i < grid.length; ++i)
            for (int j = 0; j < grid[0].length; ++j)
                if (grid[i][j] == '#') ++part1Answer;

        printGrid();

        System.out.println("The answer to part 1 is: " + part1Answer);


        /* Part 2 */

        TreeMap<Integer, List<Interval>> intervals = new TreeMap<>();
        int mini = 0, minj = 0;
        long total = 0;

        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input18.txt"));
            String line;
            int currenti = 0, currentj = 0;

            // Iterate thru instructions, generate horizontal intervals. i values will be calculated on the fly
            while ((line = in.readLine()) != null) {
                String[] splitBySpace = line.split("\\s+");
                if (splitBySpace.length != 3) continue;

                int hexCode = Integer.parseInt(splitBySpace[2].replaceAll("[^0-9A-Fa-f]", ""), 16);
                int distance = hexCode / 16;

                total += distance;

                switch (hexCode % 16) {
                    case 0: //RIGHT
                        if (!intervals.containsKey(currenti))
                            intervals.put(currenti, new ArrayList<Interval>());
                        intervals.get(currenti).add(new Interval(currentj, currentj + distance));
                        currentj += distance;
                        break;

                    case 2: //LEFT
                        if (!intervals.containsKey(currenti))
                            intervals.put(currenti, new ArrayList<Interval>());
                        intervals.get(currenti).add(new Interval(currentj - distance, currentj));
                        currentj -= distance;
                        break;

                    case 1: //DOWN
                        currenti += distance;
                        break;

                    case 3: //UP
                        currenti -= distance;
                        break;
                }

                mini = Math.min(mini, currenti);
                minj = Math.min(minj, currentj);

            }
        } catch (IOException e) { e.printStackTrace(); }


        // intervals has some negative coords so normalize them to be only positive
        int maxj = 0;
        TreeMap<Integer, List<Interval>> normalizedIntervals = new TreeMap<>();

        for (Map.Entry<Integer, List<Interval>> entry : intervals.entrySet()) {
            int row = entry.getKey();
            Interval[] currentLayer = entry.getValue().toArray(new Interval[0]); // 0 means dynamically determine size

            normalizedIntervals.put(row, new ArrayList<Interval>());
            for (Interval in : currentLayer) {
                normalizedIntervals.get(row).add(new Interval(in.startj - minj, in.endj - minj));
                maxj = Math.max(maxj, in.endj - minj);
            }
        }

        // iterate through intervals, tallying up area

        boolean[] inLagoon = new boolean[maxj + 1];
        int prev = 0;

        for (Map.Entry<Integer, List<Interval>> entry : normalizedIntervals.entrySet()) {
            // Calculate area of this chunk, excluding the current row
            int row = entry.getKey();
            int rows = row - prev - 1;
            prev = row;

            int count = 0;
            for (boolean b : inLagoon)
                if (b) ++count;
            total += (long)count * (long)rows;

            /* Calculate area of the current row
             * if the interval is expanding the lagoon horizontally, don't add to current row area
             * if the interval is reducing the lagoon horizontally, delete from current row area
             */
            boolean[] currentRow = Arrays.copyOf(inLagoon, inLagoon.length);

            for (Interval in : entry.getValue()) {
                // update the inLagoon for the next chunk
                for (int i = in.startj + 1; i < in.endj; ++i)
                    inLagoon[i] = !inLagoon[i];

                if (in.startj > 0 && inLagoon[in.startj - 1])
                    inLagoon[in.startj] = !inLagoon[in.startj];

                if (in.endj < inLagoon.length - 1 && inLagoon[in.endj + 1])
                    inLagoon[in.endj] = !inLagoon[in.endj];

                // update current row
                for (int i = in.startj; i <= in.endj; ++i)
                    currentRow[i] = inLagoon[i] && currentRow[i];
            }

            count = 0;
            for (boolean b : currentRow)
                if (b) ++count;
            total += (long)count;

        }

        System.out.println("The answer to part 2 is: " + total);

    }
}
