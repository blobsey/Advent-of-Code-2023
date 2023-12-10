import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

enum HandType {
    FIVE(0x600000),
    FOUR(0x500000),
    FULL(0x400000),
    THREE(0x300000),
    TWO(0x200000),
    ONE(0x100000),
    HIGH(0x000000);

    private final int rank;
    private HandType(int rank) {
        this.rank = rank;
    }

    public int getRank() { return rank; }
}

public class Day07 {
    static int cardToHex(char c) {
        switch (c) {
            case 'A': return 0x00000E;
            case 'K': return 0x00000D;
            case 'Q': return 0x00000C;
            case 'J': return 0x00000B;
            case 'T': return 0x00000A;
            case '9': return 0x000009;
            case '8': return 0x000008;
            case '7': return 0x000007;
            case '6': return 0x000006;
            case '5': return 0x000005;
            case '4': return 0x000004;
            case '3': return 0x000003;
            case '2': return 0x000002;
            default: return 0x000000;
        }
    }

    static int cardToHexWildcards(char c) {
        switch (c) {
            case 'A': return 0x00000E;
            case 'K': return 0x00000D;
            case 'Q': return 0x00000C;
            case 'T': return 0x00000A;
            case '9': return 0x000009;
            case '8': return 0x000008;
            case '7': return 0x000007;
            case '6': return 0x000006;
            case '5': return 0x000005;
            case '4': return 0x000004;
            case '3': return 0x000003;
            case '2': return 0x000002;
            case 'J': return 0x000001;
            default: return 0x000000;
        }
    }

    static int rank(String s) {
        HandType current = HandType.HIGH;
        int[] hand = new int[0x00000F];
        int rank = 0x000000;

        for (int i = 0; i < s.length(); ++i) {
            int card = cardToHex(s.charAt(i));
            rank += (card * (int)Math.pow(0x000010, s.length() - i - 1));

            switch (++hand[card]) {
                case 5:
                    current = HandType.FIVE;
                    break;
                case 4:
                    current = HandType.FOUR;
                    break;
                case 3:
                    if (current == HandType.TWO) current = HandType.FULL;
                    else current = HandType.THREE;
                    break;
                case 2:
                    if (current == HandType.THREE) current = HandType.FULL;
                    else if (current == HandType.ONE) current = HandType.TWO;
                    else current = HandType.ONE;
                    break;
                default:
                    break;
            }
        }
        rank += current.getRank();
        return rank;
    }

    static int rankWildcards(String s) {
        HandType current = HandType.HIGH;
        int[] hand = new int[0x00000F];
        int rank = 0x000000;
        int jokers = 0;

        for (int i = 0; i < s.length(); ++i) {
            int card = cardToHex(s.charAt(i));
            if (card == cardToHex('J')) {
                ++jokers;
                continue;
            }
            rank += (card * (int)Math.pow(0x000010, s.length() - i - 1));

            switch (++hand[card]) {
                case 5:
                    current = HandType.FIVE;
                    break;
                case 4:
                    current = HandType.FOUR;
                    break;
                case 3:
                    if (current == HandType.TWO) current = HandType.FULL;
                    else current = HandType.THREE;
                    break;
                case 2:
                    if (current == HandType.THREE) current = HandType.FULL;
                    else if (current == HandType.ONE) current = HandType.TWO;
                    else current = HandType.ONE;
                    break;
                default:
                    break;
            }
        }

        while (jokers-- > 0)
            switch (current) {
                case HandType.HIGH:
                    current = HandType.ONE;
                    break;
                case HandType.ONE:
                    current = HandType.THREE;
                    break;
                case HandType.TWO:
                    current = HandType.FULL;
                    break;
                case HandType.THREE:
                    current = HandType.FOUR;
                    break;
                case HandType.FOUR:
                    current = HandType.FIVE;
                    break;
                default:
                    break;
            }


        rank += current.getRank();
        return rank;
    }

    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("input07.txt"));
            String line;
            TreeMap<Integer, Integer> hands = new TreeMap<>();
            TreeMap<Integer, Integer> handsWildcards = new TreeMap<>();
            while ((line = in.readLine()) != null) {
                String[] lineArray = line.split("\\s+");
                int bid = Integer.parseInt(lineArray[1]);
                hands.put(rank(lineArray[0]), bid);
                handsWildcards.put(rankWildcards(lineArray[0]), bid);
            }

            int entryNum = 0;
            long winnings = 0;
            for (Map.Entry<Integer, Integer> hand : hands.entrySet()) winnings += (++entryNum) * hand.getValue();

            int entryNumWildcards = 0;
            long winningsWildcards = 0;
            for (Map.Entry<Integer, Integer> hand : handsWildcards.entrySet()) winningsWildcards += (++entryNumWildcards) * hand.getValue();


            System.out.println("The answer to part 1 is: " + winnings);
            System.out.println("The answer to part 2 is: " + winningsWildcards);

        } catch (IOException e) { e.printStackTrace(); }
    }
}