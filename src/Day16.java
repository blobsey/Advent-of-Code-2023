import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


enum Direction {
    UP, RIGHT, LEFT, DOWN
}

class Move {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return i == move.i && j == move.j && direction == move.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, direction);
    }

    public int i;
    public int j;
    public Direction direction;

    public Move(int i, int j, Direction direction) {
        this.i = i;
        this.j = j;
        this.direction = direction;
    }

    public Move next() {
        switch (direction) {
            case Direction.UP: return new Move(i - 1, j, direction);
            case Direction.DOWN: return new Move(i + 1, j, direction);
            case Direction.LEFT: return new Move(i, j - 1, direction);
            case Direction.RIGHT: return new Move(i, j + 1, direction);
            default: return null;
        }
    }

    public Move up() {
        return new Move(i - 1, j, Direction.UP);
    }

    public Move down() {
        return new Move(i + 1, j, Direction.DOWN);
    }

    public Move left() {
        return new Move(i, j - 1, Direction.LEFT);
    }

    public Move right() {
        return new Move(i, j + 1, Direction.RIGHT);
    }
}

public class Day16 {

    private static boolean isEnergized(char c) {
        return (
                    c == '#'
                    || c == 'N'
                    || c == 'Z'
                    || c == '+'
                    || c == '!'
                );
    }

    private static boolean isInBounds(char[][] grid, int i, int j) {
        return (
                grid.length != 0
                && grid[0].length != 0
                && i >= 0
                && i < grid.length
                && j >= 0
                && j < grid[0].length
                );
    }

    private static int countEnergized(char[][] g, Move start) {
        char[][] grid = Arrays.stream(g).map(char[]::clone).toArray(char[][]::new);

        Queue<Move> bfs = new LinkedList<>();
        bfs.add(start);

        Set<Move> bounces = new HashSet<Move>();

        while (!bfs.isEmpty()) {
            Move currentMove = bfs.poll();
            int i = currentMove.i;
            int j = currentMove.j;
            Direction d = currentMove.direction;

            if (!isInBounds(grid, i, j) || bounces.contains(currentMove)) continue;

            switch (grid[i][j]) {
                case 'Z':
                case '/':
                    bounces.add(currentMove);
                    grid[i][j] = 'Z';
                    switch (d) {
                        case Direction.RIGHT:
                            bfs.add(currentMove.up());
                            break;
                        case Direction.DOWN:
                            bfs.add(currentMove.left());
                            break;
                        case Direction.UP:
                            bfs.add(currentMove.right());
                            break;
                        case Direction.LEFT:
                            bfs.add(currentMove.down());
                    }
                    break;
                case 'N':
                case '\\':
                    bounces.add(currentMove);
                    grid[i][j] = 'N';
                    switch (d) {
                        case Direction.RIGHT:
                            bfs.add(currentMove.down());
                            break;
                        case Direction.DOWN:
                            bfs.add(currentMove.right());
                            break;
                        case Direction.UP:
                            bfs.add(currentMove.left());
                            break;
                        case Direction.LEFT:
                            bfs.add(currentMove.up());
                    }
                    break;
                case '!':
                case '|':
                    bounces.add(currentMove);
                    grid[i][j] = '!';
                    if (d == Direction.LEFT || d == Direction.RIGHT) {
                        bfs.add(currentMove.up());
                        bfs.add(currentMove.down());
                    }
                    else
                        bfs.add(currentMove.next());
                    break;
                case '+':
                case '-':
                    bounces.add(currentMove);
                    grid[i][j] = '+';
                    if (d == Direction.UP || d == Direction.DOWN) {
                        bfs.add(currentMove.left());
                        bfs.add(currentMove.right());
                    }
                    else
                        bfs.add(currentMove.next());
                    break;
                case '#':
                case '.':
                    grid[i][j] = '#';
                    bfs.add(currentMove.next());
                    break;
            }
        }

        int result = 0;

        for (char[] row : grid)
            for (char c : row)
                if (isEnergized(c)) ++result;

        return result;

    }

    public static void main(String[] args) {
        ArrayList<char[]> tempGrid = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input16.txt"));
            String line;
            while ((line = in.readLine()) != null) tempGrid.add(line.toCharArray());
        } catch (IOException e) { e.printStackTrace(); }

        char[][] grid = tempGrid.toArray(new char[tempGrid.size()][]);



        int part1Answer = countEnergized(grid, new Move(0, 0, Direction.RIGHT));

        System.out.println("The answer to part 1 is: " + part1Answer);


        /* Part 2 */

        int part2Answer = 0;
        for (int i = 0; i < grid.length; ++i) {
            part2Answer = Math.max(part2Answer, countEnergized(grid, new Move(i, 0, Direction.RIGHT)));
            part2Answer = Math.max(part2Answer, countEnergized(grid, new Move(i, grid[0].length - 1, Direction.LEFT)));
        }
        for (int j = 0; j < grid[0].length; ++j) {
            part2Answer = Math.max(part2Answer, countEnergized(grid, new Move(0, j, Direction.DOWN)));
            part2Answer = Math.max(part2Answer, countEnergized(grid, new Move(grid.length - 1, j, Direction.UP)));
        }

        System.out.println("The answer to part 2 is: " + part2Answer);
    }
}
