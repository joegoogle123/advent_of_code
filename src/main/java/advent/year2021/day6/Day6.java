package advent.year2021.day6;

import advent.AdventConstants;
import advent.AdventOfCodeRunner;
import advent.AdventOfCodeSolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day6 extends AdventOfCodeSolver {


    private static final Integer NEW_FISH = 8;
    private static final Integer RESET_FISH = 6;
    private static final Integer ZERO = 0;

    @Override
    public Optional<String> solvePart1() {
        LinkedList<Integer> fishes = Arrays.stream(asList().iterator().next().split(",")).map(Integer::valueOf).collect(Collectors.toCollection(() -> new LinkedList<>()));
        var iter = 0;
        while (iter++ < 80) {
            ListIterator<Integer> listIterator = fishes.listIterator();
            int newFishCount = 0;
            while (listIterator.hasNext()) {
                var next = listIterator.next();
                if (next.equals(ZERO)) {
                    newFishCount++;
                    listIterator.set(RESET_FISH);
                } else {
                    listIterator.set(next - 1);
                }
            }

            for (int i = 0; i < newFishCount; i++) {
                fishes.add(NEW_FISH);
            }
        }

        return fromNumber(fishes.size());
    }

    @Override
    public Optional<String> solvePart2() {
        var timeUntilReproduction = Arrays.stream(asList().iterator().next().split(",")).map(Integer::valueOf).toList();
        long[] frequencyArray = new long[9];

        for (Integer time : timeUntilReproduction) {
            frequencyArray[time] += 1;
        }

        for (int i = 0; i < 256; i++) {
            frequencyArray = nextDay(frequencyArray);
        }

        return fromNumber(Arrays.stream(frequencyArray).sum());
    }

    /**
     * Shifts all frequencies to the left, representing a day has passed.
     * Take the frequency at index 0 and copy it over at index 8 representing newly reproduced fish
     * Take the frequency at index 0 and add it to the frequency at index 6 representing newly added fish that have 7 turns
     * until they will procreate once again
     *
     * @param frequencyArray: Frequency array ranging from index 0 to 8 representing the time left in days until a fish can reproduce
     * @return frequencyArray after a day has passed
     */
    private long[] nextDay(long[] frequencyArray) {
        // save and zero out when timer hits 0
        var newFishCount = frequencyArray[0];

        for (int i = 1; i < frequencyArray.length; i++) {
            frequencyArray[i - 1] = frequencyArray[i];
        }

        frequencyArray[6] += newFishCount;
        frequencyArray[8] = newFishCount;
        return frequencyArray;
    }

    public static void main(String[] args) {
        var runner = AdventOfCodeRunner.getRunner(AdventConstants.YEAR_2021, AdventConstants.DAY6);
        var solver = new Day6();
        runner.solve(solver);
    }
}
