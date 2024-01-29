import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day19 {

    private static class Rule {
        char category;
        char operator;
        int value;
        String output;

        public Rule(char category, char operator, int value, String output) {
            this.category = category;
            this.operator = operator;
            this.value = value;
            this.output = output;
        }
    }

    private static class Workflow {
        ArrayList<Rule> rules;
        String defaultCondition;

        public Workflow() {
            rules = new ArrayList<>();
        }

        String process(Map<Character, Integer> part) {
            String result;
            for (Rule rule : rules) {
                int input = part.get(rule.category);

                switch(rule.operator) {
                    case '<':
                        if (input < rule.value)
                            return rule.output;
                        break;
                    case '>':
                        if (input > rule.value)
                            return rule.output;
                        break;
                }
            }
            return defaultCondition;
        }
    }

    private static class State {
        String label;
        Map<Character, int[]> intervals;

        public State(String label, Map<Character, int[]> intervals) {
            this.label = label;
            this.intervals = intervals;
        }
    }

    private static long processP2 (Map<String, Workflow> workflows) {
        Queue<State> bfs = new LinkedList<>();
        char[] labels = {'x', 'm', 'a', 's'};
        long result = 0;

        // Add the beginning state, starting at "in" and with all intervals initialized to {1, 4000}
        bfs.add(new State(
                "in",
                IntStream.range(0, labels.length).mapToObj(i -> labels[i]).collect(Collectors.toMap(
                        c -> c,
                        c -> new int[]{1, 4000}
                ))
        ));

        WORKFLOW: while (!bfs.isEmpty()) {
            State state = bfs.poll();

            switch (state.label) { // If ACCEPTED or REJECTED
                case "A":
                    long combinations = 1;
                    for (Map.Entry<Character, int[]> entry : state.intervals.entrySet())
                        combinations *= entry.getValue()[1] - entry.getValue()[0] + 1;
                    result += combinations;
                    continue;
                case "R":
                    continue;
            }

            // If not ACCEPTED or REJECTED, calculate next states from rules

            Workflow workflow = workflows.get(state.label);

            for (Rule rule : workflow.rules) {
//                System.out.println(String.format("Rule: %c %c %d : %s, x[%d %d] m[%d %d] a[%d %d] s[%d %d]",
//                        rule.category, rule.operator, rule.value, rule.output,
//                        state.intervals.get('x')[0], state.intervals.get('x')[1],
//                        state.intervals.get('m')[0], state.intervals.get('m')[1],
//                        state.intervals.get('a')[0], state.intervals.get('a')[1],
//                        state.intervals.get('s')[0], state.intervals.get('s')[1]
//                ));
                Map<Character, int[]> intervals = new HashMap<>();
                // copy unchanging intervals
                for (char c : labels) {
                    if (rule.category == c)
                        continue;
                    else
                        intervals.put(c, Arrays.copyOf(state.intervals.get(c), 2));
                }

                int start = state.intervals.get(rule.category)[0];
                int end = state.intervals.get(rule.category)[1];

                switch(rule.operator) {
                    case '<':
                        if (rule.value < start) // if rule threshold is less than entire interval, this rule doesn't do anything
                            continue;
                        else if (rule.value > end) { // if rule threshold is greater than entire interval, this rule would apply to whole interval
                            bfs.add(new State(rule.output, state.intervals));
                            continue WORKFLOW;
                        }
                        else {
                            intervals.put(rule.category, new int[]{start, rule.value - 1});
                            state.intervals.get(rule.category)[0] = rule.value;
                        }

                        break;

                    case '>':
                        if (rule.value < start) { // if rule threshold is less than entire interval, this rule would apply to whole interval
                            bfs.add(new State(rule.output, state.intervals));
                            continue WORKFLOW;
                        }
                        else if (rule.value > end) // if rule threshold is greater than entire interval, this rule would do nothing
                            continue;
                        else {
                            intervals.put(rule.category, new int[]{rule.value + 1, end});
                            state.intervals.get(rule.category)[1] = rule.value;
                        }

                        break;

                    default:
                        throw new IllegalArgumentException("Encountered non '<' or '>' character when parsing workflows");
                }

                bfs.add(new State(rule.output, intervals));
            }

            bfs.add(new State(workflow.defaultCondition, state.intervals));
        }

        return result;
    }

    public static void main(String[] args) {
        Map<String, Workflow> workflows = new HashMap<>();
        ArrayList<Map<Character, Integer>> parts = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader("input/input19.txt"))) {
            String line;
            // Read workflows (first section)
            while (!(line = in.readLine().trim()).isEmpty()) { // Go unil blank line
                Workflow currentWorkflow = new Workflow();
                String[] splitByBraces = line.split("\\{|\\}");

                String label = splitByBraces[0];
                String workflowString = splitByBraces[1];

                String[] splitByCommas = workflowString.split(",");
                for (int i = 0; i < splitByCommas.length - 1; ++i) {
                    char category = splitByCommas[i].charAt(0);
                    char operator = splitByCommas[i].charAt(1);

                    String[] terms = splitByCommas[i].split("[<>:]");
                    final int num = Integer.parseInt(terms[1]);
                    final String output = terms[2];
                    currentWorkflow.rules.add(new Rule(category, operator, num, output));
                }

                currentWorkflow.defaultCondition = splitByCommas[splitByCommas.length - 1];
                workflows.put(label, currentWorkflow);
            }

            // Read in all parts (second section)
            while ((line = in.readLine()) != null) { // Go until EOF
                String[] splitByCommasBraces = line.substring(1, line.length() - 1).split(",");
                HashMap<Character, Integer> part = new HashMap<>();

                for (String s : splitByCommasBraces) {
                    char category = s.charAt(0);
                    int value = Integer.parseInt(s.substring(2));
                    part.put(category, value);
                }
                parts.add(part);
            }


            // Process parts
            long result = 0;

            for (Map<Character, Integer> part : parts) {
                String label = "in";
                OUTER: while (true) {
                    switch (label) {
                        case "A":
                            result += part.values().stream().mapToInt(Integer::intValue).sum();
                            break OUTER;
                        case "R":
                            break OUTER;
                        default:
                            Workflow next = workflows.get(label);
                            label = next.process(part);
                    }
                }
            }

            System.out.println("The answer to part 1 is: " + result);

            System.out.println("The answer to part 2 is: " + processP2(workflows));


        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
