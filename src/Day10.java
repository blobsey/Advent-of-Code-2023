import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day10 {

    static char[][] tiles;
    static char[][] tiles3x;


    private static void printTiles(char[][] tiles) {
        Map<Character, Character> lookup = Map.of(
                '|', '║',
                '-', '═',
                'L', '╚',
                'J', '╝',
                '7', '╗',
                'F', '╔',
                '.', '.',
                'S', 'S',
                '*', '*',
                ' ', ' '
        );
        for (char[] arr : tiles) {
            for (char c : arr)
                System.out.print(lookup.get(c));
            System.out.println();
        }
    }

    private static void searchAround(int i, int j, Queue<int[]> moves) {
        if (tiles3x.length == 0 || tiles3x[0].length == 0) return;
        int maxi = tiles3x.length, maxj = tiles3x[0].length;
        Set<Character> walls = Set.of('*');
        tiles3x[i][j] = '*';
        if (i > 0 && !walls.contains(tiles3x[i-1][j])) {
            moves.add(new int[]{i-1, j});
            tiles3x[i-1][j] = '*';
        }
        if (i < maxi - 1 && !walls.contains(tiles3x[i+1][j])) {
            moves.add(new int[]{i+1, j});
            tiles3x[i+1][j] = '*';
        }
        if (j > 0 && !walls.contains(tiles3x[i][j-1])) {
            moves.add(new int[]{i, j-1});
            tiles3x[i][j-1] = '*';
        }
        if (j < maxj - 1 && !walls.contains(tiles3x[i][j+1])) {
            moves.add(new int[]{i, j+1});
            tiles3x[i][j+1] = '*';
        }
    }


    private static int[] getNextMove(int i, int j, char[][] tiles) {
        if (tiles.length == 0 || tiles[0].length == 0) return new int[]{};
        int maxi = tiles.length, maxj = tiles[0].length;

        boolean checkUp = (i > 0 && tiles[i-1][j] != '*');
        boolean checkDown = (i < maxi - 1 && tiles[i+1][j] != '*');
        boolean checkLeft = (j > 0 && tiles[i][j-1] != '*');
        boolean checkRight = (j < maxj - 1 && tiles[i][j+1] != '*');
        switch(tiles[i][j]) {
            case '|':
                if (checkUp) return new int[]{i-1, j};
                if (checkDown) return new int[]{i+1, j};
                break;
            case '-':
                if (checkLeft) return new int[]{i, j-1};
                if (checkRight) return new int[]{i, j+1};
                break;
            case 'L':
                if (checkUp) return new int[]{i-1, j};
                if (checkRight) return new int[]{i, j+1};
                break;
            case 'J':
                if (checkUp) return new int[]{i-1, j};
                if (checkLeft) return new int[]{i, j-1};
                break;
            case '7':
                if (checkDown) return new int[]{i+1, j};
                if (checkLeft) return new int[]{i, j-1};
                break;
            case 'F':
                if (checkDown) return new int[]{i+1, j};
                if (checkRight) return new int[]{i, j+1};
                break;
            default:
                return new int[]{};
        }
        return new int[]{};
    }

    public static void main(String[] args) {

        String line;
        int index = 0;
        int si = 0, sj = 0;
        List<char[]> tempTiles = new ArrayList<>();
        List<char[]> tempTiles3x = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("input/input10.txt"));
            int locationS;
            while ((line = in.readLine()) != null) {
                locationS = line.indexOf("S");
                if (locationS != -1) {
                    si = index;
                    sj = locationS;
                }
                tempTiles.add(line.toCharArray());
                ++index;

                // PART 2
                char[] out1 = new char[line.length() * 3];
                char[] out2 = new char[line.length() * 3];
                char[] out3 = new char[line.length() * 3];

                for (int i = 0; i < line.length(); ++i) {
                    int x = i * 3;
                    switch (line.charAt(i)) {
                        case 'S':
                        case '|':
                            out1[x] = ' '; out1[x+1] = '|'; out1[x+2] = ' ';
                            out2[x] = ' '; out2[x+1] = '|'; out2[x+2] = ' ';
                            out3[x] = ' '; out3[x+1] = '|'; out3[x+2] = ' ';
                            break;
                        case '-':
                            out1[x] = ' '; out1[x+1] = ' '; out1[x+2] = ' ';
                            out2[x] = '-'; out2[x+1] = '-'; out2[x+2] = '-';
                            out3[x] = ' '; out3[x+1] = ' '; out3[x+2] = ' ';
                            break;
                        case 'L':
                            out1[x] = ' '; out1[x+1] = '|'; out1[x+2] = ' ';
                            out2[x] = ' '; out2[x+1] = 'L'; out2[x+2] = '-';
                            out3[x] = ' '; out3[x+1] = ' '; out3[x+2] = ' ';
                            break;
                        case 'J':
                            out1[x] = ' '; out1[x+1] = '|'; out1[x+2] = ' ';
                            out2[x] = '-'; out2[x+1] = 'J'; out2[x+2] = ' ';
                            out3[x] = ' '; out3[x+1] = ' '; out3[x+2] = ' ';
                            break;
                        case '7':
                            out1[x] = ' '; out1[x+1] = ' '; out1[x+2] = ' ';
                            out2[x] = '-'; out2[x+1] = '7'; out2[x+2] = ' ';
                            out3[x] = ' '; out3[x+1] = '|'; out3[x+2] = ' ';
                            break;

                        case 'F':
                            out1[x] = ' '; out1[x+1] = ' '; out1[x+2] = ' ';
                            out2[x] = ' '; out2[x+1] = 'F'; out2[x+2] = '-';
                            out3[x] = ' '; out3[x+1] = '|'; out3[x+2] = ' ';
                            break;
                        case '.':
                            out1[x] = ' '; out1[x+1] = ' '; out1[x+2] = ' ';
                            out2[x] = ' '; out2[x+1] = '.'; out2[x+2] = ' ';
                            out3[x] = ' '; out3[x+1] = ' '; out3[x+2] = ' ';
                            break;
//                        case 'S':
//                            out1[x] = ' '; out1[x+1] = ' '; out1[x+2] = ' ';
//                            out2[x] = ' '; out2[x+1] = 'S'; out2[x+2] = ' ';
//                            out3[x] = ' '; out3[x+1] = ' '; out3[x+2] = ' ';
//                            break;
                    }
                }
                tempTiles3x.add(out1);
                tempTiles3x.add(out2);
                tempTiles3x.add(out3);
            }
        } catch (IOException e) { e.printStackTrace(); }

        tiles = tempTiles.toArray(new char[tempTiles.size()][]);
        tiles3x = tempTiles3x.toArray(new char[tempTiles.size()][]);


        Queue<int[]> bfs = new LinkedList<>();
        if (si > 0 && Set.of('|', 'F', '7').contains(tiles[si - 1][sj])) bfs.add(new int[]{si - 1, sj});
        if (si < tiles.length - 1 && Set.of('|', 'J', 'L').contains(tiles[si + 1][sj])) bfs.add(new int[]{si + 1, sj});
        if (sj > 0 && Set.of('-','L','F').contains(tiles[si][sj - 1])) bfs.add(new int[]{si, sj - 1});
        if (sj < tiles[0].length - 1 && Set.of('-', 'J', '7').contains(tiles[si][sj + 1])) bfs.add(new int[]{si, sj + 1});
        tiles[si][sj] = '*';

        int steps = 0;
        while (!bfs.isEmpty()) {
            for (int i = 0; i < bfs.size(); ++i) {
                int[] tmp = bfs.poll();
                int[] next = getNextMove(tmp[0], tmp[1], tiles);
                if (next.length == 2) bfs.add(next);
                tiles[tmp[0]][tmp[1]] = '*';
            }

            ++steps;
        }

        System.out.println("The answer to part 1 is: " + (steps - 1));


        // PART 2

        si = (si * 3) + 1;
        sj = (sj * 3) + 1;

        bfs = new LinkedList<>();
        if (si > 0 && Set.of('|', 'F', '7').contains(tiles3x[si - 1][sj])) bfs.add(new int[]{si - 1, sj});
        if (si < tiles3x.length - 1 && Set.of('|', 'J', 'L').contains(tiles3x[si + 1][sj])) bfs.add(new int[]{si + 1, sj});
        if (sj > 0 && Set.of('-','L','F').contains(tiles3x[si][sj - 1])) bfs.add(new int[]{si, sj - 1});
        if (sj < tiles3x[0].length - 1 && Set.of('-', 'J', '7').contains(tiles3x[si][sj + 1])) bfs.add(new int[]{si, sj + 1});
        tiles3x[si][sj] = '*';

        while (!bfs.isEmpty()) {
            for (int i = 0; i < bfs.size(); ++i) {
                int[] tmp = bfs.poll();
                int[] next = getNextMove(tmp[0], tmp[1], tiles3x);
                if (next.length == 2) bfs.add(next);
                tiles3x[tmp[0]][tmp[1]] = '*';
            }
        }

        Queue<int[]> moves = new LinkedList<>();
        moves.add(new int[]{0, 0});

        while (!moves.isEmpty()) {
            int[] move = moves.poll();
            int i = move[0], j = move[1];
            tiles3x[i][j] = '*';
            searchAround(i, j, moves);
        }

        int part2answer = 0;
        for (char[] arr : tiles3x)
            for (char c : arr)
                if (c != '*' && c != ' ')
                    if (c == '.') part2answer += 3;
                    else ++part2answer;

        printTiles(tiles3x);

        System.out.println("The answer to part 2 is: " + (part2answer / 3));



    }
}
