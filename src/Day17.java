import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

class Day17 {

    static int[][] grid;
    static int[][] losses;
    static int ROWS;
    static int COLS;
    
    static boolean DEBUG = false;

    private static void printGrid() {
        for (int[] row : losses) {
            for (int cell : row) {
                if (cell == Integer.MAX_VALUE) {
                    System.out.print("###\t");
                } else {
                    System.out.print(cell + "\t");
                }
            }
            System.out.println();
        }
        System.out.println();
        try {int temp = System.in.read();}
        catch (IOException e) {}
    }

    private enum Direction {
        UP(0), RIGHT(1), DOWN(2), LEFT(3);

        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public Direction rotateClockwise() {
            return values()[(this.ordinal() + 1) % values().length];
        }

        public Direction rotateCounterClockwise() {
            return values()[(this.ordinal() - 1 + values().length) % values().length];
        }
    }
    private static class Step implements Comparable<Step> {
        public int i;
        public int j;
        public int loss;
        public Direction direction;
        public int stepsInDirection;

        public Step(int i, int j, int loss, Direction direction, int stepsInDirection) {
            this.i = i;
            this.j = j;
            this.loss = loss;
            this.direction = direction;
            this.stepsInDirection = stepsInDirection;
        }


        public Step calculateStep(Direction next_direction) {
            int next_i = i, next_j = j;
            switch (next_direction) {
                case UP: next_i -= 1; break;
                case DOWN: next_i += 1; break;
                case LEFT: next_j -= 1; break;
                case RIGHT: next_j += 1; break;
            }
            int next_loss = loss + grid[next_i][next_j];
            int next_stepsInDirection = (this.direction == next_direction) ? stepsInDirection + 1 : 1;

            return new Step(next_i, next_j, next_loss, next_direction, next_stepsInDirection);
        }

        public boolean canMoveP1(Direction next_direction) {
            int next_i = i, next_j = j;
            switch (next_direction) {
                case UP: next_i -= 1; break;
                case DOWN: next_i += 1; break;
                case LEFT: next_j -= 1; break;
                case RIGHT: next_j += 1; break;
            }

            // Bounds checking
            if (next_i < 0 || next_i >= ROWS || next_j < 0 || next_j >= COLS)
                return false;

            // Don't go more than 3 times in the same direction
            if (this.direction == next_direction && stepsInDirection >= 3)
                return false;

            return true;
        }

        public boolean canMoveP2(Direction next_direction) {
            int next_i = i, next_j = j;
            switch (next_direction) {
                case UP: next_i -= 1; break;
                case DOWN: next_i += 1; break;
                case LEFT: next_j -= 1; break;
                case RIGHT: next_j += 1; break;
            }

            // Bounds checking
            if (next_i < 0 || next_i >= ROWS || next_j < 0 || next_j >= COLS)
                return false;

            // Don't go more than 10 times in the same direction
            if (this.direction == next_direction && stepsInDirection >= 10)
                return false;

            // Don't turn if less than 4 times in the same direction
            if (this.direction != next_direction && stepsInDirection < 4)
                return false;

            return true;
        }

        // Override compareTo because we will put Step objects in a priorityQueue
        @Override
        public int compareTo(Step o) {
            return Integer.compare(this.loss, o.loss);
        }

        // Override equals and hashCode because we will be using a visited set
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Step step = (Step) o;
            return i == step.i && j == step.j && stepsInDirection == step.stepsInDirection && direction == step.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j, direction, stepsInDirection);
        }
    }


    private static int solveP1() {
        PriorityQueue<Step> priorityQueue = new PriorityQueue<>();
        HashSet<Step> visited = new HashSet<>();

        priorityQueue.add(new Step(0, 0, 0, Direction.RIGHT, 0));
        priorityQueue.add(new Step(0, 0, 0, Direction.DOWN, 0));

        while (!priorityQueue.isEmpty()) {
            Step current = priorityQueue.poll();
            Direction direction = current.direction;

            if (current.i == ROWS - 1 && current.j == COLS - 1)
                return current.loss;

            if (visited.contains(current))
                continue;

            if (DEBUG) {
                losses[current.i][current.j] = Math.min(losses[current.i][current.j], current.loss);
                printGrid();
            }

            visited.add(current);

            Direction[] directions = new Direction[]{direction, direction.rotateClockwise(), direction.rotateCounterClockwise()};
            for (Direction next_direction : directions) {
                if (current.canMoveP1(next_direction))
                    priorityQueue.add(current.calculateStep(next_direction));
            }
        }

        return -1;
    }

    private static int solveP2() {
        PriorityQueue<Step> priorityQueue = new PriorityQueue<>();
        HashSet<Step> visited = new HashSet<>();

        priorityQueue.add(new Step(0, 0, 0, Direction.RIGHT, 0));
        priorityQueue.add(new Step(0, 0, 0, Direction.DOWN, 0));

        while (!priorityQueue.isEmpty()) {
            Step current = priorityQueue.poll();
            Direction direction = current.direction;

            if (current.i == ROWS - 1 && current.j == COLS - 1 && current.stepsInDirection >= 4)
                return current.loss;

            if (visited.contains(current))
                continue;

            if (DEBUG) {
                losses[current.i][current.j] = Math.min(losses[current.i][current.j], current.loss);
                printGrid();
            }

            visited.add(current);

            Direction[] directions = new Direction[]{direction, direction.rotateClockwise(), direction.rotateCounterClockwise()};
            for (Direction next_direction : directions) {
                if (current.canMoveP2(next_direction))
                    priorityQueue.add(current.calculateStep(next_direction));
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        ArrayList<int[]> tempGrid = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input17.txt"));
            String line;
            while ((line = in.readLine()) != null)
                tempGrid.add(line.chars().map(c -> c - '0').toArray());
        }
        catch (IOException e) { e.printStackTrace(); }

        grid = tempGrid.stream().toArray(int[][]::new);
        ROWS = grid.length;
        COLS = grid[0].length;

        if (DEBUG) {
            losses = IntStream.range(0, ROWS)
                    .mapToObj(i -> IntStream.generate(() -> Integer.MAX_VALUE)
                            .limit(COLS)
                            .toArray())
                    .toArray(int[][]::new);
        }

        System.out.println("The answer to part 1 is: " + solveP1());
        System.out.println("The answer to part 2 is: " + solveP2());

    }
}