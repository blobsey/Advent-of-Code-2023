import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day04 {

    private static List<Integer> cards;
    static { cards = new ArrayList<Integer>(); }
    public static void addOrAppend(int index, int value) {
        while (index >= cards.size()) cards.add(0);
        cards.set(index, cards.get(index) + value);
    }

    public static void main(String[] args) {

        try (BufferedReader in = new BufferedReader(new FileReader("input/input04.txt"))) {
            String line;
            int totalScore = 0, totalCards = 0, totalCardsFinal = 0;

            int cardNumber = 0;
            while ((line = in.readLine()) != null) {

                addOrAppend(cardNumber, 1);

                // split Card off of line
                // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                // [Card 1][41 48 83 86 17 | 83 86  6 31 17  9 48 53]
                //         [41 48 83 86 17] [83 86  6 31 17  9 48 53]
                String[] ingest;
                try { ingest = line.split("Card\\s*\\d+:\\s*")[1].split("\\s*\\|\\s*"); }
                catch (ArrayIndexOutOfBoundsException e) { continue; }


                // Read first half into array, second half into Set
                int[] myNumbers = Arrays.stream(
                        ingest[0].split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();

                Set<Integer> winningNumbers = Arrays.stream(ingest[1].split("\\s+"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());


                // iterate through myNumbers, if there is match update score
                int matches = 0;
                for (int number : myNumbers)
                    matches += (winningNumbers.contains(number)) ? 1 : 0;


                for (int i = 1; i <= matches; ++i)
                    addOrAppend(cardNumber + i, cards.get(cardNumber));

                totalCards += cards.get(cardNumber);

                cardNumber++;



                totalScore += (int)(Math.pow(2, (matches - 1)));
            }


            System.out.println("The answer to part 1 is: " + totalScore);
            System.out.println("The answer to part 2 is: " + totalCards);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}