import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

class IntervalMapping {
    long dest;
    long start;
    long range;

    public IntervalMapping(long dest, long start, long range) {
        this.dest = dest;
        this.start = start;
        this.range = range;
    }

    public boolean contains(long num) {
        return (num >= start && num < start + range);
    }

    public long getDest(long num) {
        return (dest + (num - start));
    }
}

public class Day05 {
    public static void readMapping(String header, BufferedReader in, TreeMap<Long, IntervalMapping> mapping, TreeMap<Long, IntervalMapping> backMapping) throws IOException {
        String line = in.readLine();
        while (line != null & !line.startsWith(header)) line = in.readLine();

        while ((line = in.readLine()) != null) {
            try {
                long[] input = Arrays.stream(line.split("\\s+"))
                        .mapToLong(Long::parseLong)
                        .toArray();
                mapping.put(input[1], new IntervalMapping(input[0], input[1], input[2]));
                backMapping.put(input[0], new IntervalMapping(input[1], input[0], input[2]));
            }
            catch (ArrayIndexOutOfBoundsException e) {
                return;
            }
            catch (NumberFormatException e) {
                return;
            }
        }

    }

    public static void main(String[] args) {

        try (BufferedReader in = new BufferedReader(new FileReader("input/input05.txt"))) {

            String line = in.readLine();
            if (line == null) return;

            long[] seeds = Arrays.stream(line.split("seeds:\\s+")[1].split("\\s+"))
                    .mapToLong(Long::parseLong)
                    .toArray();

            ArrayList<IntervalMapping> seedIntervals = new ArrayList<>();
            for (int i = 0; i < seeds.length; i += 2) seedIntervals.add(new IntervalMapping(seeds[i], seeds[i], seeds[i+1]));

            TreeMap[] maps = new TreeMap[7];
            TreeMap[] backMaps = new TreeMap[7];
            String[] headers = {
                    "seed-to-soil map:",
                    "soil-to-fertilizer map:",
                    "fertilizer-to-water map:",
                    "water-to-light map:",
                    "light-to-temperature map:",
                    "temperature-to-humidity map:",
                    "humidity-to-location map:"
            };

            for (int i = 0; i < maps.length; ++i) {
                maps[i] = new TreeMap<Long, IntervalMapping>();
                backMaps[i] = new TreeMap<Long, IntervalMapping>();
                readMapping(headers[i], in, maps[i], backMaps[i]);
            }

            for (int i = 0; i < seeds.length; ++i) {
                for (int j = 0; j < maps.length; ++j) {
                    Map.Entry<Long, IntervalMapping> entry = maps[j].floorEntry(seeds[i]);
                    if (entry != null && entry.getValue().contains(seeds[i])) seeds[i] = entry.getValue().getDest(seeds[i]);
                }
            }

            long min = seeds[0];
            for (long seed : seeds)
                min = Math.min(min, seed);

            long location = -1;
            boolean found = false;
            OUTER: for (long i = 0; i < Long.MAX_VALUE; ++i) {
                location = i;
                long backMapInput = i;
                for (int j = maps.length - 1; j >= 0; --j) {
                    Map.Entry<Long, IntervalMapping> entry = backMaps[j].floorEntry(backMapInput);
                    if (entry != null && entry.getValue().contains(backMapInput)) backMapInput = entry.getValue().getDest(backMapInput);
                }
                for (IntervalMapping seedInterval : seedIntervals)
                    if (seedInterval.contains(backMapInput)) {
                        found = true;
                        break OUTER;
                    }
            }

            System.out.println("The answer to part 1 is: " + min);
            if (found) System.out.println("The answer to part 2 is: " + location);
            else System.out.println("There is no answer to part 2.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}